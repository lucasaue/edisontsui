using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using System.Windows.Forms;

namespace SimpleCFDModelViewer
{
    class InfoBox
    {
        private static InfoBox instance;
        private RichTextBox m_infoBox;

        private InfoBox() { }

        public static InfoBox Instance
        {
          get 
          {
             if (instance == null)
             {
                 instance = new InfoBox();
             }
             return instance;
          }
        }

        public void Init(RichTextBox infoBox)
        {
            m_infoBox = infoBox;
        }

        public void WriteLine(String str)
        {
            if (m_infoBox != null)
                m_infoBox.Text += str+"\n";
        }
    }
}
