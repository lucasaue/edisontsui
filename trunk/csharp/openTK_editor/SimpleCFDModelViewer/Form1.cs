/**********************************************************
 * Simple XML file viewer using OpenTK
 * 
 * Author: Edison       Email: edisontsui@gmail.com
 * */

using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

using OpenTK;
using OpenTK.Graphics;
using OpenTK.Graphics.OpenGL;
using System.Diagnostics;

namespace SimpleCFDModelViewer
{
    public partial class Form1 : Form
    {
        #region variable

        private const float m_near = 1.0f;
        private const float m_far = 10000.0f;

        private Dictionary<GLControl, int> m_glControlViewList;
        private Simple3DWorld m_simple3DWorld;

        private GLControl m_currentGLControl;
        private bool m_updateCameraControl;
        private int m_lastMouseX;
        private int m_lastMouseY;
        private int m_lastWheel;
        private MODE m_mode;

        private Stopwatch m_timer;

        #endregion

        public Form1()
        {
            // init var
            m_glControlViewList = new Dictionary<GLControl, int>();
            m_updateCameraControl = false;
            m_currentGLControl = null;

            // init component
            InitializeComponent();
            Init3DWorld();

            // listener
            this.MouseDown += this.ControlMouseDown;
            this.MouseUp += this.ControlMouseUp;
            this.MouseWheel += this.ControlMouseWheel;

            // Mainloop
            Application.Idle += new EventHandler(MainLoop);

            // Timer
            m_timer = new Stopwatch();
            m_timer.Start();

            // InfoBox
            InfoBox.Instance.Init(uiInfoLabel);
        }

        #region LOGIC
        private void MainLoop(object sender, EventArgs e)
        {
            double elapsedTimeMS = GetElapsedTimeMS();
            UpdateCamera(elapsedTimeMS);
            m_simple3DWorld.Tick(elapsedTimeMS);
            uiFpsLabel.Text = ((int)(1000 / elapsedTimeMS)).ToString();

        }
        private double GetElapsedTimeMS()
        {
            m_timer.Stop();
            double timeslice = m_timer.Elapsed.TotalMilliseconds;
            m_timer.Reset();
            m_timer.Start();
            return timeslice;
        }
        private void UpdateCamera(double dt)
        {
            //InfoBox.Instance.WriteLine("MainLoop");
            if (m_currentGLControl == null)
                return;
            Camera camera;
            m_simple3DWorld.GetCamera(m_glControlViewList[m_currentGLControl], out camera);

            float deltaX = (float)((Control.MousePosition.X - m_lastMouseX) *dt/1000);
            float deltaY = (float)((Control.MousePosition.Y - m_lastMouseY) * dt/1000);
            float deltaWheel = (float)m_lastWheel;
            m_lastMouseX = Control.MousePosition.X;
            m_lastMouseY = Control.MousePosition.Y;
            m_lastWheel = 0;
            //InfoBox.Instance.WriteLine("DeltaX:" + deltaX+" DeltaY:"+deltaY);
            if (m_updateCameraControl)
            {
                camera.Control(m_mode, deltaX, deltaY, deltaWheel);
                m_currentGLControl.Invalidate();
                deltaWheel = 0;

                uiCamPosLabel.Text = camera.m_translation.ToString();
                uiTargetPosLabel.Text = camera.m_target.ToString();
            }
            if (deltaWheel != 0)
            {
                camera.Control(m_mode, 0, 0, deltaWheel);
                m_currentGLControl.Invalidate();
            }
        }
        #endregion

        #region Menu_Function
        private void exitToolStripMenuItem_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void openToolStripMenuItem_Click(object sender, EventArgs e)
        {
            uiOpenFile.Title = "Open CFD Model File";
            uiOpenFile.Filter = "XML Files|*.xml";
            uiOpenFile.FileName = "";
            uiOpenFile.InitialDirectory = Application.ExecutablePath;
                //System.Environment.GetFolderPath(Environment.SpecialFolder.Personal);

            if (uiOpenFile.ShowDialog() != DialogResult.Cancel)
            {
                uiFileLabel.Text = uiOpenFile.FileName;
                if (m_simple3DWorld != null)
                {
                    m_simple3DWorld.AddModel(uiOpenFile.FileName);
                    InvalidAllView();
                }
            }
        }

        private void saveToolStripMenuItem_Click(object sender, EventArgs e)
        {
            uiSaveFile.Title = "Save PNG";
            uiSaveFile.Filter = "PNG Files|*.png";
            uiSaveFile.FileName = "";
            uiSaveFile.InitialDirectory = Application.ExecutablePath; ;

            if (uiSaveFile.ShowDialog() != DialogResult.Cancel)
            {
                uiFileLabel.Text = uiSaveFile.FileName;
            }
        }

        private void translateToolStripMenuItem_Click(object sender, EventArgs e)
        {
            m_mode = MODE.MODE_TRANSLATE;
            uiModeLabel.Text = "Trans";
        }

        private void rotateToolStripMenuItem_Click(object sender, EventArgs e)
        {
            m_mode = MODE.MODE_ROTATE;
            uiModeLabel.Text = "Rotate";
        }

        private void resetTargetPosToolStripMenuItem_Click(object sender, EventArgs e)
        {
            if (m_currentGLControl == null)
                return;
            Camera camera;
            m_simple3DWorld.GetCamera(m_glControlViewList[m_currentGLControl], out camera);
            camera.m_target = new Vector3(0, 0, 0);
            camera.Control(MODE.MODE_TRANSLATE, 0, 0, 0);
            m_currentGLControl.Invalidate();
        }

        private void onToolStripMenuItem_Click(object sender, EventArgs e)
        {
            m_simple3DWorld.m_enableTrident = true;
            InvalidAllView();
        }

        private void offToolStripMenuItem_Click(object sender, EventArgs e)
        {
            m_simple3DWorld.m_enableTrident = false;
            InvalidAllView();
        }

        private void onToolStripMenuItem1_Click(object sender, EventArgs e)
        {
            m_simple3DWorld.m_enableGrid = true;
            InvalidAllView();
        }

        private void offToolStripMenuItem1_Click(object sender, EventArgs e)
        {
            m_simple3DWorld.m_enableGrid = false;
            InvalidAllView();
        }

        private void solidToolStripMenuItem_Click(object sender, EventArgs e)
        {
            if (m_simple3DWorld != null)
                m_simple3DWorld.m_polygonMode = PolygonMode.Fill;
            InvalidAllView();
        }

        private void wireframeToolStripMenuItem_Click(object sender, EventArgs e)
        {
            if (m_simple3DWorld != null)
                m_simple3DWorld.m_polygonMode = PolygonMode.Line;
            InvalidAllView();
        }

        private void pointToolStripMenuItem_Click(object sender, EventArgs e)
        {
            if (m_simple3DWorld != null)
                m_simple3DWorld.m_polygonMode = PolygonMode.Point;
            InvalidAllView();
        }
        private void onToolStripMenuItem2_Click(object sender, EventArgs e)
        {
            if (m_simple3DWorld != null)
                m_simple3DWorld.ShowModelBoundingBox(true);
            InvalidAllView();
        }

        private void offToolStripMenuItem2_Click(object sender, EventArgs e)
        {
            if (m_simple3DWorld != null)
                m_simple3DWorld.ShowModelBoundingBox(false);
            InvalidAllView();
        }
        #endregion

        #region UI_Function
        /// <summary>
        /// Bind the two Splitter
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void uiSplitTop_SplitterMoved(object sender, SplitterEventArgs e)
        {
            uiSplitTop.SplitterDistance = e.SplitX;
            uiSplitBottom.SplitterDistance = e.SplitX;
        }
        /// <summary>
        /// Form resize
        /// update the size and pos of glControl & panel
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void Form1_Resize(object sender, EventArgs e)
        {
            uiSplitContainer.Width = this.Width - uiTab.Width - 5;
            uiSplitContainer.Height = uiTab.Height = this.Height - uiMenuStrip.Height - statusStrip1.Height - 5;
            // put seperator to middle
            uiSplitTop.Panel1MinSize = uiSplitTop.Panel2MinSize = uiSplitContainer.Width / 2;
            uiSplitTop.Panel1MinSize = uiSplitTop.Panel2MinSize = 10;

            uiTab.Location = new Point(uiSplitContainer.Width + 5, uiTab.Location.Y);
        }
        #endregion

        #region glControl_Function
        /// <summary>
        /// Focus Function, taking record of current panel
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void GLControlEnter(object sender, EventArgs e)
        {
            m_currentGLControl = sender as GLControl;
        }
        private void GLControlLeave(object sender, EventArgs e)
        {
            //m_currentGLControl = null;
        }

        /// <summary>
        /// **PanelLoad - Add new View & Camera
        ///             - Bind to glControl for rendering
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void TopViewPanelLoad(object sender, EventArgs e)
        {
            try
            {
                GLControl glControl = sender as GLControl;
                int cameraId = m_simple3DWorld.AddCamera(new Vector3(0, 0, 0), new Vector2(0, 0), 5);
                m_simple3DWorld.AddView(cameraId,VIEW_PERSPECTIVE_TYPE.VIEW_ORTHNO, glControl.Width, glControl.Height, m_near, m_far);
                m_glControlViewList.Add(glControl, cameraId);

                m_simple3DWorld.Init();
            }
            catch
            {
                InfoBox.Instance.WriteLine("glControl load error");
            }
        }
        private void FrontViewPanelLoad(object sender, EventArgs e)
        {
            try
            {
                GLControl glControl = sender as GLControl;
                int cameraId = m_simple3DWorld.AddCamera(new Vector3(0, 0, 0), new Vector2(90, 90), 5);
                m_simple3DWorld.AddView(cameraId, VIEW_PERSPECTIVE_TYPE.VIEW_ORTHNO, glControl.Width, glControl.Height, m_near, m_far);
                m_glControlViewList.Add(glControl, cameraId);
            }
            catch
            {
                InfoBox.Instance.WriteLine("glControl load error");
            }
        }
        private void RightViewPanelLoad(object sender, EventArgs e)
        {
            try
            {
                GLControl glControl = sender as GLControl;
                int cameraId = m_simple3DWorld.AddCamera(new Vector3(0, 0, 0), new Vector2(0, 90), 5);
                m_simple3DWorld.AddView(cameraId, VIEW_PERSPECTIVE_TYPE.VIEW_ORTHNO, glControl.Width, glControl.Height, m_near, m_far);
                m_glControlViewList.Add(glControl, cameraId);
            }
            catch
            {
                InfoBox.Instance.WriteLine("glControl load error");
            }
        }
        private void PerspectivePanelLoad(object sender, EventArgs e)
        {
            try
            {
                GLControl glControl = sender as GLControl;
                int cameraId = m_simple3DWorld.AddCamera(new Vector3(0, 0, 0), new Vector2(0, 45), 500);
                m_simple3DWorld.AddView(cameraId, VIEW_PERSPECTIVE_TYPE.VIEW_PERSPECTIVE, glControl.Width, glControl.Height, m_near, m_far);
                m_glControlViewList.Add(glControl, cameraId);
            }
            catch
            {
                InfoBox.Instance.WriteLine("glControl load error");
            }
        }

        /// <summary>
        /// Paint - render function
        ///       - call 3D world paint func
        ///        - force redraw
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void GLControlPaint(object sender, PaintEventArgs e)
        {
            try
            {
                GLControl glControl = sender as GLControl;
                glControl.MakeCurrent();
                int cameraId = m_glControlViewList[glControl];
                m_simple3DWorld.Render(cameraId);
                glControl.SwapBuffers();
                //InfoBox.Instance.WriteLine("Paint:"+cameraId);
            }
            catch
            {
                InfoBox.Instance.WriteLine("Paint error");
            }
        }

        /// <summary>
        /// Resize function
        /// reset the viewport
        /// force redraw
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void GLControlResize(object sender, EventArgs e)
        {
            try
            {
                GLControl glControl = sender as GLControl;
                int cameraId = m_glControlViewList[glControl];
                m_simple3DWorld.UpdateView(cameraId, glControl.Width, glControl.Height);
                glControl.Invalidate();
            }
            catch
            {
            }
        }
        #endregion

        #region UserInput_Function
        private void ControlMouseDown(object sender, MouseEventArgs e)
        {
            m_updateCameraControl = true;
            //InfoBox.Instance.WriteLine("CamControl:" + m_updateCameraControl);
            
        }
        private void ControlMouseUp(object sender, MouseEventArgs e)
        {
            m_updateCameraControl = false;
            //InfoBox.Instance.WriteLine("CamControl:" + m_updateCameraControl);
        }
        private void ControlMouseWheel(object sender, MouseEventArgs e)
        {
            if (e.Delta > 0)
            {
                m_lastWheel++;
            }
            else if (e.Delta < 0)
            {
                m_lastWheel--;
            }
        }
        #endregion

        #region 3D_World
        /// <summary>
        /// setup world
        /// add model
        /// </summary>
        private void Init3DWorld()
        {
            m_simple3DWorld = new Simple3DWorld();
        }
        #endregion

        #region Helper
        private void InvalidAllView()
        {
            uiGlControlBottomLeft.Invalidate();
            uiGlControlBottomRight.Invalidate();
            uiGlControlTopLeft.Invalidate();
            uiGlControlTopRight.Invalidate();
        }
        #endregion



   


    }
}
