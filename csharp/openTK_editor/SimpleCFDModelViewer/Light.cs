using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using OpenTK;
using OpenTK.Graphics;
using OpenTK.Graphics.OpenGL;
using OpenTK.Platform;

using System.Drawing;   // Color

namespace SimpleCFDModelViewer
{
    public class DirectionLight
    {
        private double m_angle = 0;
        public Vector3 m_direction { get; set; }
        public DirectionLight()
        {
            m_direction = new Vector3();
        }

        public void PrepareLight()
        {
            //InfoBox.Instance.WriteLine("Light:" + m_direction.ToString());
            GL.Enable(EnableCap.Lighting);
            GL.Enable(EnableCap.Light0);
            GL.Light(LightName.Light0, LightParameter.Position, new Color4(m_direction.X, m_direction.Y, m_direction.Z, 0));   // define a directional light
            GL.Light(LightName.Light0, LightParameter.Diffuse, Color.Green);
            //GL.Light(LightName.Light0, LightParameter.Specular, Color.LightGreen);
        }
    }
}
