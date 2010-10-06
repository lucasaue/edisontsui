using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using OpenTK;
using OpenTK.Graphics;
using OpenTK.Graphics.OpenGL;

using System.Drawing;   // Color

namespace SimpleCFDModelViewer
{
    class Simple3DWorld
    {
        #region variable

        // List of Camera for lookup and render
        private Dictionary<int, Camera> m_cameraList;
        private int m_cameraCount;

        // Supporting Options, trident, grid, polygon mode
        public bool m_enableTrident {get; set;}
        public bool m_enableGrid {get; set;}
        private Trident m_trident;
        private Grid m_grid;
        public PolygonMode m_polygonMode { get; set; }

        // direction light follow camera
        private DirectionLight m_light;

        // terrain model
        private VBOObject m_model;

        private Sprite m_sprite;

        #endregion

        public Simple3DWorld()
        {
            m_cameraList = new Dictionary<int, Camera>();
            m_cameraCount = 0;

            m_enableTrident = true;
            m_enableGrid = true;
        }

        public void Init()
        {
            m_polygonMode = PolygonMode.Fill;

            m_trident = new Trident(100, 5);
            m_grid = new Grid(50, 50, 100);
            m_model = null;

            m_light = new DirectionLight();

            // test
            //m_sprite = new Sprite();
            //m_sprite.SetupTexture("Data/Textures/logo.jpg");
            //m_sprite.m_scale = new Vector3(1000, 1000, 1000);
        }

        #region Camera_View_Function
        public void AddView(int cameraId, VIEW_PERSPECTIVE_TYPE perspective, int width, int height, float near, float far)
        {
            try
            {
                m_cameraList[cameraId].m_view = new View(perspective, width, height, near, far, (float)Math.PI / 4, 1);
            }
            catch
            {
                InfoBox.Instance.WriteLine("Error 2001 - Camera not exist");
            }
        }

        public int AddCamera(Vector3 position, Vector2 angle, float radius)
        {
            int currentCount = m_cameraCount++;
            Camera camera = new Camera(position, angle, radius);
            m_cameraList.Add(currentCount, camera);
            return currentCount;
         }

        public void GetCamera(int cameraId, out Camera camera)
        {
            camera = m_cameraList[cameraId];
        }

        public void UpdateView(int cameraId, int width, int height)
        {
            try
            {
                m_cameraList[cameraId].m_view.m_width = width;
                m_cameraList[cameraId].m_view.m_height = height;
            }
            catch
            {
            }
        }
        #endregion

        public void Render(int cameraId)
        {
            try
            {
                // setup mode
                GL.ClearColor(Color.White);
                GL.PolygonMode(MaterialFace.FrontAndBack, m_polygonMode);
                //GL.Enable(EnableCap.CullFace);
                //GL.CullFace(CullFaceMode.Front);
                GL.Enable(EnableCap.DepthTest);
                GL.ShadeModel(ShadingModel.Smooth);
               
                View view = m_cameraList[cameraId].m_view;
                Camera camera = m_cameraList[cameraId];

                // setup Projection Matrix according to View
                view.SetupProjectionMatrix();

                // setup ModelView Matrix for camera
                camera.SetupCameraMatrix();

                // Render scene
                GL.Clear(ClearBufferMask.ColorBufferBit | ClearBufferMask.DepthBufferBit);
                
                // Light
                m_light.m_direction = m_cameraList[3].m_fwdVec;
                m_light.PrepareLight();

                // Model
                if (m_enableTrident)
                    m_trident.Render();
                if (m_enableGrid)
                    m_grid.Render();

                if(m_model != null)
                    m_model.Render();

                //m_sprite.Render();
            }
            catch
            {
                InfoBox.Instance.WriteLine("Error 3001 - render error");
            }
        }

        public void Tick(double timeMs)
        {
        }

        public void AddModel(String fileName)
        {
            m_model = null;
            m_model = new VBOObject();
            m_model.InitVBOObject(fileName);
        }
        public void ShowModelBoundingBox(bool visible)
        {
            if (m_model == null)
                return;
            m_model.ShowBoundingBox(visible);
        }

    }
}
