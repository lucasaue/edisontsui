using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using OpenTK;
using OpenTK.Graphics;
using OpenTK.Graphics.OpenGL;
using OpenTK.Platform;

using System.Drawing;   // Color
using System.Drawing.Imaging;   // Bitmap

using System.Runtime.InteropServices;

using System.Xml;
using System.Xml.Linq;

namespace SimpleCFDModelViewer
{

    public class Node
    {
        public Vector3 m_translation { get; set; }
        public Vector3 m_rotation { get; set; }
        public Vector3 m_scale { get; set; }
        protected List<Node> m_childList;
        public Node m_parent { get; set; }

        public Node()
        {
            m_childList = new List<Node>();
            m_translation = m_rotation = new Vector3(0, 0, 0);
            m_scale = new Vector3(1, 1, 1);
        }

        public void AttachChild(Node node)
        {
            if (node.m_parent != null)
                node.m_parent.DetachChild(node);
            m_childList.Add(node);
            node.m_parent = this;
        }
        public void DetachChild(Node node)
        {
            if (node.m_parent != this)
                return;
            m_childList.Remove(node);
            node.m_parent = null;
        }
        public void DetachAllChild()
        {
            foreach (Node node in m_childList)
                node.m_parent = null;
            m_childList.Clear();
        }

    }
 
    public class BaseObject : Node
    {
        // texture
        protected Bitmap m_bitmap;
        protected int m_texture;
        // privot point
        public Vector3 m_pivot { get; set; }

        public BaseObject() :base()
        {
            m_pivot = new Vector3(0, 0, 0);
        }
        /*
        ~BaseObject()
        {
            if (m_bitmap != null)
            {
                GL.DeleteTextures(1, ref m_texture);
                m_bitmap.Dispose();
            }
        }
         */
        public virtual void Render(){
            GL.PushMatrix();

            // Translate
            GL.Translate(m_translation);
            // Offset to privot point and rotate and scale
            GL.Translate(-m_pivot);
            // Rotate
            GL.Rotate(m_rotation.X, Vector3.UnitX);
            GL.Rotate(m_rotation.Y, Vector3.UnitY);
            GL.Rotate(m_rotation.Z, Vector3.UnitZ);
            // Scale
            GL.Scale(m_scale);
            GL.Translate(m_pivot);

            //GL.PushAttrib(AttribMask.TextureBit);
            //if(m_bitmap != null)
            //    GL.BindTexture(TextureTarget.Texture2D, m_texture);     // Apply Texture

            // Render self
            DrawObject();

            //GL.PopAttrib();
            
            // Render child
            foreach (Node node in m_childList)
            {
                BaseObject obj = node as BaseObject;
                if (obj != null)
                    obj.Render();
            }

            GL.PopMatrix();
        }
        public virtual void DrawObject()
        {
        }
        public void SetupTexture(String file)
        {
            try
            {
                m_bitmap = new Bitmap(file);
                if (m_bitmap != null)
                    PrepareTexture();
            }
            catch
            {
                InfoBox.Instance.WriteLine("Error 3001 - Texture:" + file);
            }
        }
        private void PrepareTexture()
        {
            GL.Enable(EnableCap.Texture2D);

            GL.Hint(HintTarget.PerspectiveCorrectionHint, HintMode.Nicest);

            GL.GenTextures(1, out m_texture);
            GL.BindTexture(TextureTarget.Texture2D, m_texture);

            BitmapData data = m_bitmap.LockBits(new System.Drawing.Rectangle(0, 0, m_bitmap.Width, m_bitmap.Height),
                ImageLockMode.ReadOnly, System.Drawing.Imaging.PixelFormat.Format32bppArgb);

            GL.TexImage2D(TextureTarget.Texture2D, 0, PixelInternalFormat.Rgba, data.Width, data.Height, 0,
                OpenTK.Graphics.OpenGL.PixelFormat.Bgra, PixelType.UnsignedByte, data.Scan0);

            m_bitmap.UnlockBits(data);

            GL.TexParameter(TextureTarget.Texture2D, TextureParameterName.TextureMinFilter, (int)TextureMinFilter.Linear);
            GL.TexParameter(TextureTarget.Texture2D, TextureParameterName.TextureMagFilter, (int)TextureMagFilter.Linear);
        }
    }

    public class BoundingBox : BaseObject
    {
        private float[] m_min, m_max;
        private float m_width = 1.0f;
        public BoundingBox(float[] min, float[] max, float width)
        {
            m_min = min;
            m_max = max;
            m_width = width;
        }
        public override void DrawObject()
        {
            DrawBoundingBox();
        }

        private void DrawBoundingBox()
        {
            // define the vertex index for 6 face of a cube
            int[,] faces = new int[6, 4]
          {
            {0, 1, 2, 3},
            {3, 2, 6, 7},
            {7, 6, 5, 4},
            {4, 5, 1, 0},
            {5, 6, 2, 1},
            {7, 4, 0, 3}
          };
            // define the vertices
            float[,] v = new float[8, 3];
            int i;


            v[0, 2] = v[1, 2] = v[2, 2] = v[3, 2] = m_min[2];      // z
            v[4, 2] = v[5, 2] = v[6, 2] = v[7, 2] = m_max[2];
            v[0, 1] = v[1, 1] = v[4, 1] = v[5, 1] = m_min[1];
            v[2, 1] = v[3, 1] = v[6, 1] = v[7, 1] = m_max[1];
            v[0, 0] = v[3, 0] = v[4, 0] = v[7, 0] = m_min[0];
            v[1, 0] = v[2, 0] = v[5, 0] = v[6, 0] = m_max[0];

            GL.PushMatrix();
            GL.PushAttrib(AttribMask.LineBit | AttribMask.LightingBit | AttribMask.PolygonBit);
            GL.Disable(EnableCap.Lighting);
            GL.LineWidth(m_width);
            GL.PolygonMode(MaterialFace.FrontAndBack, PolygonMode.Line);
            // use openGL to draw 6 faces [Quad (4 vertice polygon)]
            for (i = 5; i >= 0; i--)
            {
                GL.Begin(BeginMode.Quads);
                GL.Color3(1.0f, 0.0f, 0.0f); GL.Vertex3(ref v[faces[i, 0], 0]);
                GL.Color3(1.0f, 0.0f, 0.0f); GL.Vertex3(ref v[faces[i, 1], 0]);
                GL.Color3(1.0f, 0.0f, 0.0f); GL.Vertex3(ref v[faces[i, 2], 0]);
                GL.Color3(1.0f, 0.0f, 0.0f); GL.Vertex3(ref v[faces[i, 3], 0]);
                GL.End();
            }
            GL.PopAttrib();
            GL.PopMatrix();
        }
    }

    public class Trident : BaseObject
    {
        private float m_scale;
        private float m_width;

        private Cone[] m_cone = new Cone[3];

        public Trident(float scale, float width) 
        {
            m_scale = scale;
            m_width = width;

            // X cone
            m_cone[0] = new Cone(false, Color.Blue);
            m_cone[0].m_rotation = new Vector3(0, 0, -90);
            m_cone[0].m_translation = new Vector3(m_scale, 0, 0);
            m_cone[0].m_scale = new Vector3(scale, scale, scale);
            this.AttachChild(m_cone[0]);

            // Y cone
            m_cone[1] = new Cone(false, Color.Green);
            m_cone[1].m_translation = new Vector3(0, m_scale, 0);
            m_cone[1].m_scale = new Vector3(scale, scale, scale);
            this.AttachChild(m_cone[1]);


            // Z cone
            m_cone[2] = new Cone(false, Color.Red);
            m_cone[2].m_rotation = new Vector3(90, 0, 0);
            m_cone[2].m_translation = new Vector3(0, 0, m_scale);
            m_cone[2].m_scale = new Vector3(scale, scale, scale);
            this.AttachChild(m_cone[2]);

        }
        public override void DrawObject()
        {
            DrawTrident(m_scale, m_width);
        }
        
        private void DrawTrident(float scale, float width)
        {
            GL.PushMatrix();
            GL.PushAttrib(AttribMask.LineBit | AttribMask.LightingBit);
            GL.Disable(EnableCap.Lighting);
            GL.LineWidth(width);
            GL.Scale(new Vector3(scale, scale, scale));
            GL.Begin(BeginMode.Lines);
            // X
            GL.Color3(Color.Blue); 
            GL.Vertex3(Vector3.Zero);
            GL.Vertex3(Vector3.UnitX);
            // Y
            GL.Color3(Color.Green);
            GL.Vertex3(Vector3.Zero);
            GL.Vertex3(Vector3.UnitY);
            // Z
            GL.Color3(Color.Red);
            GL.Vertex3(Vector3.Zero);
            GL.Vertex3(Vector3.UnitZ);
            GL.End();

            GL.PopAttrib();
            GL.PopMatrix();
        }
    }

    public class Cone : BaseObject
    {
        private int m_numFace = 36;
        private float m_height = 0.4f;
        private float m_radius = 0.1f;
        private bool m_enableLighting;
        private Color m_color;

        public Cone(bool enableLighting, Color color) 
        {
            m_enableLighting = enableLighting;
            m_color = color;
        }
        public override void DrawObject()
        {
            DrawCone();   
        }
        private void DrawCone()
        {
            GL.PushAttrib(AttribMask.LightingBit);
            if (m_enableLighting == true)
            {
                GL.Enable(EnableCap.Lighting);
            }
            else
            {
                GL.Disable(EnableCap.Lighting);
                GL.Color3(m_color);
            }
            GL.Begin(BeginMode.TriangleFan);
            GL.Vertex3(new Vector3(0, m_height, 0));
            for(int i=0; i<m_numFace; ++i)
            {
                float currentAngle = (float)(i*2*Math.PI/m_numFace);
                GL.Vertex3(new Vector3((float)(m_radius*Math.Sin(currentAngle)), 0, (float)(m_radius*Math.Cos(currentAngle))));
            }
            GL.Vertex3(new Vector3((float)(m_radius * Math.Sin(0)), 0, (float)(m_radius * Math.Cos(0))));
            GL.End();
            GL.PopAttrib();
        }
    }

    public class Sprite : BaseObject
    {
        public Sprite() { }

        public override void DrawObject()
        {
            //GL.Disable(EnableCap.Lighting);
            //GL.BindTexture(TextureTarget.Texture2D, m_texture);


            //GL.Begin(BeginMode.Quads);
            //GL.Normal3(0, 0, 1); GL.TexCoord2(0, 1); GL.Vertex3(new Vector3(-1, -1, 0));
            //GL.Normal3(0, 0, 1); GL.TexCoord2(1, 1); GL.Vertex3(new Vector3(1, -1, 0));
            //GL.Normal3(0, 0, 1); GL.TexCoord2(1, 0); GL.Vertex3(new Vector3(1, 1, 0));
            //GL.Normal3(0, 0, 1); GL.TexCoord2(0, 0); GL.Vertex3(new Vector3(-1, 1, 0));
            //GL.End();

            
            
            Bitmap bitmap = new Bitmap("Data/Textures/metal.jpg");

            //GL.ClearColor(Color.MidnightBlue);
            //GL.Enable(EnableCap.Texture2D);

            GL.Hint(HintTarget.PerspectiveCorrectionHint, HintMode.Nicest);
            
            GL.GenTextures(1, out m_texture);
            GL.BindTexture(TextureTarget.Texture2D, m_texture);

            BitmapData data = bitmap.LockBits(new System.Drawing.Rectangle(0, 0, bitmap.Width, bitmap.Height),
                ImageLockMode.ReadOnly, System.Drawing.Imaging.PixelFormat.Format32bppArgb);

            GL.TexImage2D(TextureTarget.Texture2D, 0, PixelInternalFormat.Rgba, data.Width, data.Height, 0,
                OpenTK.Graphics.OpenGL.PixelFormat.Bgra, PixelType.UnsignedByte, data.Scan0);

            bitmap.UnlockBits(data);

            GL.TexParameter(TextureTarget.Texture2D, TextureParameterName.TextureMinFilter, (int)TextureMinFilter.Linear);
            GL.TexParameter(TextureTarget.Texture2D, TextureParameterName.TextureMagFilter, (int)TextureMagFilter.Linear);

            if (GL.GetError() != ErrorCode.NoError)
                throw new Exception("Error loading texture " );

            GL.Disable(EnableCap.Lighting);
            GL.Color3(Color.LightBlue);
            GL.BindTexture(TextureTarget.Texture2D, m_texture);

            GL.Begin(BeginMode.Quads);

            GL.TexCoord2(0.0f, 1.0f); GL.Vertex3(-0.6f, -0.4f, 0.0f);
            GL.TexCoord2(1.0f, 1.0f); GL.Vertex3(0.6f, -0.4f, 0.0f);
            GL.TexCoord2(1.0f, 0.0f); GL.Vertex3(0.6f, 0.4f, 0.0f);
            GL.TexCoord2(0.0f, 0.0f); GL.Vertex3(-0.6f, 0.4f,0.0f);

            GL.End();

            GL.Disable(EnableCap.Texture2D);
             
        }

        private void DrawSprite() { }

    }

    public class Grid : BaseObject
    {
        private int m_rows;
        private int m_colomns;
        private int m_interval;

        public Grid(int rows, int columns, int interval)
        {
            m_rows = rows;
            m_colomns = columns;
            m_interval = interval;
        }
        public override void DrawObject()
        {
            DrawGrid(m_rows, m_colomns, m_interval);
        }
        private void DrawGrid(int rows, int columns, int interval)
        {
            //DrawPlaneGrid(rows, columns, interval, 0);
            DrawPlaneGrid(rows, columns, interval, 90);
        }

        private void DrawPlaneGrid(int rows, int columns, int interval, int rotate)
        {
            GL.PushMatrix();
            GL.PushAttrib(AttribMask.LightingBit);
            GL.Disable(EnableCap.Lighting);
            GL.LineWidth(1.0f);
            GL.Color3(Color.Black);
            GL.Scale(new Vector3(interval, interval, interval));
            GL.Rotate(rotate, Vector3.UnitX);
            /* Render grid over 0..rows, 0..columns. */
            GL.Begin(BeginMode.Lines);
            /* Horizontal lines. */
            for (int i = -rows; i <= rows; i++)
            {
                GL.Vertex3(-columns, i, 0);
                GL.Vertex3(columns, i, 0);
            }
            /* Vertical lines. */
            for (int i = -columns; i <= columns; i++)
            {
                GL.Vertex3(i, -rows, 0);
                GL.Vertex3(i, rows, 0);
            }
            GL.End();
            GL.PopAttrib();
            GL.PopMatrix();
        }
    }

    public class VBOObject : BaseObject
    {
        public float[] m_max = new float[3]{0,0,0};
        public float[] m_min = new float[3]{0,0,0};
        public Vector3 m_normalizeSize = new Vector3(500.0f, 500.0f, 100.0f);       // the size it will auto scale to 

        private struct Vbo { public int m_vertexBufferId, m_elementBufferId, m_nornalBufferId, m_numElements; }
        private Vbo m_vbo;

        private Vector3[] m_vertices;
        private Vector3[] m_surfaceNormals;
        private Vector3[] m_vertexNormals;
        private short[] m_elements;

        private BoundingBox m_boundingBox;

        public VBOObject()
        {
        }
        /// <summary>
        /// Load Element and vertex from VBO
        /// Please see debug/test.xml for format and reference
        /// </summary>
        /// <param name="fileName"></param>
        public void InitVBOObject(string fileName)
        {
            try
            {
                String str = "Loading XML: 0 -" + fileName + " preparing";
                InfoBox.Instance.WriteLine(str);
                XElement rootElement = XElement.Load(fileName);
                XElement defElement = rootElement.Element("Surfaces").Element("Surface").Element("Definition");
                int verticeCount = 0;
                int elementCount = defElement.Element("Faces").Elements().Count();
                foreach (XElement child in defElement.Element("Pnts").Elements())
                {
                    verticeCount = Math.Max(verticeCount, Int16.Parse(child.Attribute("id").Value));
                }
                verticeCount = Math.Max(verticeCount, defElement.Element("Pnts").Elements().Count());
                m_vertices = new Vector3[verticeCount];
                m_elements = new short[elementCount * 3];
                m_surfaceNormals = new Vector3[elementCount];
                m_vertexNormals = new Vector3[verticeCount];
                bool max_min_initized = false;
                foreach (XElement child in defElement.Element("Pnts").Elements())
                {
                    int id = Int16.Parse(child.Attribute("id").Value) - 1;
                    String[] posValues = (child.Value).Split();
                    float[] pos = new float[3];
                    if (posValues.Count() == 3)
                    {
                        int i = 0;
                        foreach (String posValue in posValues)
                        {
                            float val = float.Parse(posValue);
                            if (val > m_max[i] || max_min_initized==false) m_max[i] = val;
                            if (val < m_min[i] || max_min_initized==false) m_min[i] = val;
                            pos[i++] = val;
                        }
                        m_vertices[id] = new Vector3(pos[0], pos[1], pos[2]);
                        max_min_initized = true;
                    }
                }
                InfoBox.Instance.WriteLine("Loading XML: 1 - vertex info completed");
                int count = 0;
                foreach (XElement child in defElement.Element("Faces").Elements())
                {
                    String normalString = child.Attribute("n").Value;
                    float[] normalValue = ParseVec3(normalString);
                    m_surfaceNormals[count] = new Vector3(normalValue[0], normalValue[1], normalValue[2]);

                    String[] faceValues = (child.Value).Split();
                    if (faceValues.Count() == 3)
                    {
                        int i = 0;
                        foreach (String faceValue in faceValues)
                        {
                            short vertexId = (short)(int.Parse(faceValue) - 1);
                            m_elements[count * 3 + i] = vertexId;
                            m_vertexNormals[vertexId] += m_surfaceNormals[count];
                            ++i;
                        }
                        ++count;
                    }
                }
                InfoBox.Instance.WriteLine("Loading XML: 2 - element info completed");

                // normalisze vertexNormal
                for (int i = 0; i < m_vertexNormals.Length; ++i)
                {
                    m_vertexNormals[i].Normalize();
                }

                // put to center
                m_translation = new Vector3((m_max[0] - m_min[0]), (m_max[1] - m_min[1]), (m_max[2] - m_min[2]));
                m_translation = (new Vector3(m_min[0], m_min[1], m_min[2]) + m_translation / 2) * -1;
                m_pivot = m_translation;
                m_rotation = new Vector3(-90, 0, 0);
                float scaleX = m_max[0] - m_min[0]; scaleX = (scaleX!=0)?scaleX:1.0f;
                float scaleY = m_max[1] - m_min[1]; scaleY = (scaleY != 0) ? scaleY : 1.0f;
                float scaleZ = m_max[2] - m_min[2]; scaleZ = (scaleZ != 0) ? scaleZ : 1.0f;
                m_scale = new Vector3(m_normalizeSize.X / scaleX, m_normalizeSize.Y / scaleY, m_normalizeSize.Z / scaleZ); 

                // set VBO
                m_vbo = LoadVBO(m_vertices, m_elements, m_vertexNormals);
                InfoBox.Instance.WriteLine("Loading XML: 3 - VBO completed");
            }
            catch (NullReferenceException e)
            {
                InfoBox.Instance.WriteLine("Error: 1001 - XML file Error");
            }
            catch (IndexOutOfRangeException e)
            {
                InfoBox.Instance.WriteLine("Error: 1002 - Out of range");
            }
            catch
            {
                InfoBox.Instance.WriteLine("Error: 1003 - Undefined");
            }

        }


        Vbo LoadVBO(Vector3[] vertices, short[] elements, Vector3[] normals)
        {
            InfoBox.Instance.WriteLine("Prepare VBO: 0 - Start");
            Vbo handle = new Vbo();
            try
            {
                int size;

                // To create a VBO:
                // 1) Generate the buffer handles for the vertex and element buffers.
                // 2) Bind the vertex buffer handle and upload your vertex data. Check that the buffer was uploaded correctly.
                // 3) Bind the element buffer handle and upload your element data. Check that the buffer was uploaded correctly.

                // VertexBufferObject
                {
                    GL.GenBuffers(1, out handle.m_vertexBufferId);
                    GL.BindBuffer(BufferTarget.ArrayBuffer, handle.m_vertexBufferId);
                    GL.BufferData(BufferTarget.ArrayBuffer, (IntPtr)(vertices.Length * Vector3.SizeInBytes), vertices,
                                  BufferUsageHint.StaticDraw);
                    GL.GetBufferParameter(BufferTarget.ArrayBuffer, BufferParameterName.BufferSize, out size);
                    if (vertices.Length * Vector3.SizeInBytes != size)
                        throw new ApplicationException("Vertex data not uploaded correctly");
                    InfoBox.Instance.WriteLine("Prepare VBO: 1 - vertex buffer generated");
                }

                // ElementBufferObject
                {
                    GL.GenBuffers(1, out handle.m_elementBufferId);
                    GL.BindBuffer(BufferTarget.ElementArrayBuffer, handle.m_elementBufferId);
                    GL.BufferData(BufferTarget.ElementArrayBuffer, (IntPtr)(elements.Length * sizeof(short)), elements,
                                  BufferUsageHint.StaticDraw);
                    GL.GetBufferParameter(BufferTarget.ElementArrayBuffer, BufferParameterName.BufferSize, out size);
                    if (elements.Length * sizeof(short) != size)
                        throw new ApplicationException("Element data not uploaded correctly");

                    InfoBox.Instance.WriteLine("Prepare VBO: 2 - element buffer generated");
                }

                // NormalBufferObject (optional)
                if(normals != null)
                {
                    GL.GenBuffers(1, out handle.m_nornalBufferId);
                    GL.BindBuffer(BufferTarget.ArrayBuffer, handle.m_nornalBufferId);
                    GL.BufferData(BufferTarget.ArrayBuffer, (IntPtr)(normals.Length * Vector3.SizeInBytes), normals, BufferUsageHint.StaticDraw);
                    GL.GetBufferParameter(BufferTarget.ArrayBuffer, BufferParameterName.BufferSize, out size);
                    if (normals.Length * Vector3.SizeInBytes != size)
                        throw new ApplicationException("Normal data not uploaded correctly");
                }
                handle.m_numElements = elements.Length;
            }
            catch
            {
                InfoBox.Instance.WriteLine("Error 1004 - OpenGL fail to init Vertex buffer object, please try updating driver");
            }
            return handle;
        }

        void Draw(Vbo handle)
        {
            // To draw a VBO:
            // 1) Ensure that the VertexArray client state is enabled.
            // 2) Bind the vertex and element buffer handles.
            // 3) Set up the data pointers (vertex, normal, color) according to your vertex format.
            // 4) Call DrawElements. (Note: the last parameter is an offset into the element buffer
            //    and will usually be IntPtr.Zero).

            if (handle.m_vertexBufferId == 0 || handle.m_elementBufferId == 0)
                return;     // vbo, ebo not init

            // Vertex
            {
                GL.EnableClientState(EnableCap.VertexArray);
                GL.BindBuffer(BufferTarget.ArrayBuffer, handle.m_vertexBufferId);
                GL.VertexPointer(3, VertexPointerType.Float, Vector3.SizeInBytes, IntPtr.Zero);
            }
            // Normal
            if (handle.m_nornalBufferId != 0)
            {
                GL.EnableClientState(EnableCap.NormalArray);
                GL.BindBuffer(BufferTarget.ArrayBuffer, handle.m_nornalBufferId);
                GL.NormalPointer(NormalPointerType.Float, Vector3.SizeInBytes, IntPtr.Zero);
            }
            // Element
            {
                GL.BindBuffer(BufferTarget.ElementArrayBuffer, handle.m_elementBufferId);
                GL.DrawElements(BeginMode.Triangles, handle.m_numElements, DrawElementsType.UnsignedShort, IntPtr.Zero);
            }
        }

        public override void DrawObject()
        {
            // Render Both Fill & Line
            // 1 Draw the outline into the color, depth, and stencil buffers.
            // 2 Draw the filled primitive into the color buffer and depth buffer, but only where the stencil buffer is clear.
            GL.PushAttrib(AttribMask.LightingBit);
            GL.PushAttrib(AttribMask.PolygonBit);
            GL.PolygonMode(MaterialFace.FrontAndBack, PolygonMode.Line);
            GL.Enable(EnableCap.StencilTest);
            GL.StencilFunc(StencilFunction.Always,0x1, 0xff);
            GL.StencilOp(StencilOp.Keep, StencilOp.Keep, StencilOp.Incr);
            GL.Clear(ClearBufferMask.StencilBufferBit);
            GL.LineWidth(2);
            GL.Color4(Color4.Blue);
            GL.Disable(EnableCap.Lighting);
            Draw(m_vbo);
            GL.PopAttrib();
            GL.PopAttrib();

            GL.PushAttrib(AttribMask.PolygonBit);
            GL.PolygonMode(MaterialFace.FrontAndBack, PolygonMode.Fill);
            GL.Enable(EnableCap.StencilTest);
            GL.StencilFunc(StencilFunction.Notequal, 0x1, 0xff);
            GL.StencilOp(StencilOp.Keep, StencilOp.Keep, StencilOp.Replace);
            Draw(m_vbo);
            GL.PopAttrib();



            
            
            //Draw(m_vbo);
        }

        public void ShowBoundingBox(bool visible)
        {
            if (visible)
            {
                if (m_boundingBox == null)
                {
                    // test - prepare BB
                    m_boundingBox = new BoundingBox(m_min, m_max, 3.0f);
                    this.AttachChild(m_boundingBox);
                }
            }
            else
            {
                if (m_boundingBox != null)
                {
                    DetachChild(m_boundingBox);
                    m_boundingBox = null;
                }
            }

        }

        private float[] ParseVec3(string str)
        {
            float[] vec = new float[3];
            String[] strValues = str.Split();
            if (strValues.Count() == 3)
            {
                int i = 0;
                foreach (String value in strValues)
                {
                    vec[i] = float.Parse(value);
                    ++i;
                }
            }
            return vec;
        }

    }
}
