using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using OpenTK;
using OpenTK.Graphics;
using OpenTK.Graphics.OpenGL;
using OpenTK.Platform;

using System.Drawing;   // Color

using System.Drawing;
using System.Runtime.InteropServices;
using OpenTK;

namespace SimpleCFDModelViewer
{
    #region ENUM
    public enum VIEW_PERSPECTIVE_TYPE
    {
        VIEW_ORTHNO,
        VIEW_PERSPECTIVE
    }
    public enum MODE
    {
        MODE_TRANSLATE,
        MODE_ROTATE
    }
    #endregion

    /// <summary>
    /// Camera class follow target and orbit around the target by angle, radius specified
    /// </summary>
    public class Camera : Node
    {
        private const float ELIPSION = 0.001f;

        public Vector3 m_target { get; set; }
        public Vector3 m_offset { get; set; }
        public Vector2 m_angle { get; set; } // pan, tilt
        public float m_radius { get; set; }
        public Vector3 m_fwdVec { get; set; }

        protected Vector2 m_capRadius = new Vector2(0.1f, 10000.0f);
        protected Vector3 m_sensitivity = new Vector3(10.0f, 10.0f, 10.0f);    // translation, rotation, zoom

        public View m_view { get; set; }

        public Camera(Vector3 pos, Vector2 angle, float radius)
        {
            m_target = pos;
            m_angle = angle;
            m_radius = radius;

            m_offset = new Vector3(0, 0, 0);
            m_fwdVec = new Vector3(0, 0, 0);

            this.Control(MODE.MODE_ROTATE, 0, 0, 0);

        }

        public void SetupCameraMatrix()
        {
            Matrix4 modelView;
            GetModelViewMatrix(out modelView);
            GL.MatrixMode(MatrixMode.Modelview);
            GL.LoadMatrix(ref modelView);
        }
        public void GetModelViewMatrix(out Matrix4 modelView)
        {
            Vector3 upVec = Vector3.UnitY;
            m_translation = m_target + m_offset;
            Vector3 normalizedLookAt = m_offset * -1;
            normalizedLookAt.Normalize();
            // set upVec to Z axis in case camDir is in Y axis
            // check NaN also
            float dotValue = OpenTK.Vector3.Dot(normalizedLookAt, Vector3.UnitY);
            if (float.IsNaN(dotValue) || Math.Abs(dotValue) >= 1 - ELIPSION)
            {
                upVec = Vector3.UnitZ;
            }
            modelView = Matrix4.LookAt(m_translation, m_target, upVec);
        }

        public void Control(MODE mode, float mouseXDelta, float mouseYDelta, float mouseWheelDelta)
        {
            Matrix4 modelView, modelViewTranspose;
            switch (mode)
            {
                case MODE.MODE_ROTATE:
                    float angleY, angleX;
                    float offsetX, offsetY, offsetZ;

                    angleY = m_angle.Y - m_sensitivity.Y * mouseYDelta;
                    angleY = Math.Max(0, Math.Min(180, angleY));
                    angleX = m_angle.X + m_sensitivity.Y * mouseXDelta;
                    angleX %= 360;
                    m_angle = new Vector2(angleX, angleY);

                    m_radius -= m_sensitivity.Z * mouseWheelDelta;
                    m_radius = Math.Max(m_capRadius.X, Math.Min(m_capRadius.Y, m_radius));
                    offsetY = (float)(m_radius * (Math.Cos(m_angle.Y * Math.PI / 180)));
                    offsetX = (float)(m_radius * ((Math.Sin(m_angle.Y * Math.PI / 180) * Math.Cos(m_angle.X * Math.PI / 180))));
                    offsetZ = (float)(m_radius * ((Math.Sin(m_angle.Y * Math.PI / 180) * Math.Sin(m_angle.X * Math.PI / 180))));
                    m_offset = new Vector3(offsetX, offsetY, offsetZ);
                    //InfoBox.Instance.WriteLine("AngleY:" + angleY + " MouseY:" + mouseYDelta);
                    //InfoBox.Instance.WriteLine("OffsetX:" + m_offset.X+" OffsetY:"+m_offset.Y+" OffsetZ:"+m_offset.Z);
                    break;
                case MODE.MODE_TRANSLATE:
                    Vector3 tran = new Vector3(mouseXDelta, mouseYDelta, 0);
                    Vector3 rightVec = Vector3.UnitX;
                    Vector3 upVec = Vector3.UnitY;
                    Vector3 fwdVec = Vector3.UnitZ;
                    this.GetModelViewMatrix(out modelView);
                    Matrix4.Transpose(ref modelView, out modelViewTranspose);
                    Vector3 newRightVec = Vector3.TransformVector(rightVec, modelViewTranspose);
                    Vector3 newUpVec = Vector3.TransformVector(upVec, modelViewTranspose);
                    Vector3 newFwdVec = Vector3.TransformVector(fwdVec, modelViewTranspose);
                    newUpVec.Normalize();
                    newRightVec.Normalize();
                    m_target = m_target - m_sensitivity.X * mouseXDelta * newRightVec;
                    m_target = m_target + m_sensitivity.X * mouseYDelta * newUpVec;
                    this.Control(MODE.MODE_ROTATE, 0, 0, mouseWheelDelta);

                    break;
                default:
                    break;
            }

            // tmp
            this.GetModelViewMatrix(out modelView);
            Matrix4.Transpose(ref modelView, out modelViewTranspose);
            m_fwdVec = Vector3.TransformVector(Vector3.UnitZ, modelViewTranspose);
        }
    }

    /// <summary>
    /// View that control the perspective and view size
    /// </summary>
    public class View
    {
        public int m_width { get; set; }
        public int m_height { get; set; }
        public float m_near { get; set; }
        public float m_far { get; set; }
        public float m_fov { get; set; }
        public float m_orthnoScale { get; set; }
        public VIEW_PERSPECTIVE_TYPE m_perspective { get; set; }


        public View(VIEW_PERSPECTIVE_TYPE perspective, int width, int height, float near, float far, float fov, float orthnoScale)
        {
            m_width = width;
            m_height = height;
            m_near = near;
            m_far = far;
            m_perspective = perspective;
            m_fov = fov;
            m_orthnoScale = orthnoScale;
        }
        public void SetupProjectionMatrix()
        {
            GL.MatrixMode(MatrixMode.Projection);
            GL.LoadIdentity();

            switch (m_perspective)
            {
                case VIEW_PERSPECTIVE_TYPE.VIEW_ORTHNO:
                    GL.Ortho(-m_width / m_orthnoScale, m_width / m_orthnoScale, -m_height / m_orthnoScale, m_height / m_orthnoScale, m_near, m_far); // Bottom-left corner pixel has coordinate (0, 0)
                    break;
                case VIEW_PERSPECTIVE_TYPE.VIEW_PERSPECTIVE:
                    Matrix4 projection = Matrix4.CreatePerspectiveFieldOfView(m_fov, m_width / m_height, m_near, m_far);
                    GL.MatrixMode(MatrixMode.Projection);
                    GL.LoadMatrix(ref projection);
                    break;
                default:
                    break;
            }
            GL.Viewport(0, 0, m_width, m_height); // Use all of the glControl painting area
        }
    }
}
