namespace SimpleCFDModelViewer
{
    partial class Form1
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            this.uiMenuStrip = new System.Windows.Forms.MenuStrip();
            this.fileToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.openToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.saveToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.toolStripSeparator1 = new System.Windows.Forms.ToolStripSeparator();
            this.exitToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.editToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.tridentToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.onToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.offToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.gridToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.onToolStripMenuItem1 = new System.Windows.Forms.ToolStripMenuItem();
            this.offToolStripMenuItem1 = new System.Windows.Forms.ToolStripMenuItem();
            this.modeToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.wireframeToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.solidToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.pointToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.boundingBoxToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.onToolStripMenuItem2 = new System.Windows.Forms.ToolStripMenuItem();
            this.offToolStripMenuItem2 = new System.Windows.Forms.ToolStripMenuItem();
            this.helpToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.tridentToolStripMenuItem1 = new System.Windows.Forms.ToolStripMenuItem();
            this.xToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.yGreenToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.zRedToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.uiOpenFile = new System.Windows.Forms.OpenFileDialog();
            this.uiSaveFile = new System.Windows.Forms.SaveFileDialog();
            this.statusStrip1 = new System.Windows.Forms.StatusStrip();
            this.uiFpsText = new System.Windows.Forms.ToolStripStatusLabel();
            this.uiFpsLabel = new System.Windows.Forms.ToolStripStatusLabel();
            this.uiFileText = new System.Windows.Forms.ToolStripStatusLabel();
            this.uiFileLabel = new System.Windows.Forms.ToolStripStatusLabel();
            this.uiModelText = new System.Windows.Forms.ToolStripStatusLabel();
            this.uiModeLabel = new System.Windows.Forms.ToolStripStatusLabel();
            this.uiCamPosText = new System.Windows.Forms.ToolStripStatusLabel();
            this.uiCamPosLabel = new System.Windows.Forms.ToolStripStatusLabel();
            this.uiTargetPosText = new System.Windows.Forms.ToolStripStatusLabel();
            this.uiTargetPosLabel = new System.Windows.Forms.ToolStripStatusLabel();
            this.contextMenuStrip1 = new System.Windows.Forms.ContextMenuStrip(this.components);
            this.translateToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.rotateToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.toolStripSeparator2 = new System.Windows.Forms.ToolStripSeparator();
            this.resetTargetPosToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.uiTab = new System.Windows.Forms.TabControl();
            this.uiTabInfo = new System.Windows.Forms.TabPage();
            this.uiInfoLabel = new System.Windows.Forms.RichTextBox();
            this.uiTabProperty = new System.Windows.Forms.TabPage();
            this.uiSplitContainer = new System.Windows.Forms.SplitContainer();
            this.uiSplitTop = new System.Windows.Forms.SplitContainer();
            this.uiViewIndicator1 = new System.Windows.Forms.Label();
            this.uiGlControlTopLeft = new OpenTK.GLControl();
            this.uiViewIndicator2 = new System.Windows.Forms.Label();
            this.uiGlControlTopRight = new OpenTK.GLControl();
            this.uiSplitBottom = new System.Windows.Forms.SplitContainer();
            this.uiViewIndicator3 = new System.Windows.Forms.Label();
            this.uiGlControlBottomLeft = new OpenTK.GLControl();
            this.uiViewIndicator4 = new System.Windows.Forms.Label();
            this.uiGlControlBottomRight = new OpenTK.GLControl();
            this.uiMenuStrip.SuspendLayout();
            this.statusStrip1.SuspendLayout();
            this.contextMenuStrip1.SuspendLayout();
            this.uiTab.SuspendLayout();
            this.uiTabInfo.SuspendLayout();
            this.uiSplitContainer.Panel1.SuspendLayout();
            this.uiSplitContainer.Panel2.SuspendLayout();
            this.uiSplitContainer.SuspendLayout();
            this.uiSplitTop.Panel1.SuspendLayout();
            this.uiSplitTop.Panel2.SuspendLayout();
            this.uiSplitTop.SuspendLayout();
            this.uiSplitBottom.Panel1.SuspendLayout();
            this.uiSplitBottom.Panel2.SuspendLayout();
            this.uiSplitBottom.SuspendLayout();
            this.SuspendLayout();
            // 
            // uiMenuStrip
            // 
            this.uiMenuStrip.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.fileToolStripMenuItem,
            this.editToolStripMenuItem,
            this.helpToolStripMenuItem});
            this.uiMenuStrip.Location = new System.Drawing.Point(0, 0);
            this.uiMenuStrip.Name = "uiMenuStrip";
            this.uiMenuStrip.Size = new System.Drawing.Size(782, 28);
            this.uiMenuStrip.TabIndex = 0;
            this.uiMenuStrip.Text = "menuStrip1";
            // 
            // fileToolStripMenuItem
            // 
            this.fileToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.openToolStripMenuItem,
            this.saveToolStripMenuItem,
            this.toolStripSeparator1,
            this.exitToolStripMenuItem});
            this.fileToolStripMenuItem.Name = "fileToolStripMenuItem";
            this.fileToolStripMenuItem.Size = new System.Drawing.Size(44, 24);
            this.fileToolStripMenuItem.Text = "File";
            // 
            // openToolStripMenuItem
            // 
            this.openToolStripMenuItem.Name = "openToolStripMenuItem";
            this.openToolStripMenuItem.Size = new System.Drawing.Size(114, 24);
            this.openToolStripMenuItem.Text = "Open";
            this.openToolStripMenuItem.Click += new System.EventHandler(this.openToolStripMenuItem_Click);
            // 
            // saveToolStripMenuItem
            // 
            this.saveToolStripMenuItem.Name = "saveToolStripMenuItem";
            this.saveToolStripMenuItem.Size = new System.Drawing.Size(114, 24);
            this.saveToolStripMenuItem.Text = "Save";
            this.saveToolStripMenuItem.Click += new System.EventHandler(this.saveToolStripMenuItem_Click);
            // 
            // toolStripSeparator1
            // 
            this.toolStripSeparator1.Name = "toolStripSeparator1";
            this.toolStripSeparator1.Size = new System.Drawing.Size(111, 6);
            // 
            // exitToolStripMenuItem
            // 
            this.exitToolStripMenuItem.Name = "exitToolStripMenuItem";
            this.exitToolStripMenuItem.Size = new System.Drawing.Size(114, 24);
            this.exitToolStripMenuItem.Text = "Exit";
            this.exitToolStripMenuItem.Click += new System.EventHandler(this.exitToolStripMenuItem_Click);
            // 
            // editToolStripMenuItem
            // 
            this.editToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.tridentToolStripMenuItem,
            this.gridToolStripMenuItem,
            this.modeToolStripMenuItem,
            this.boundingBoxToolStripMenuItem});
            this.editToolStripMenuItem.Name = "editToolStripMenuItem";
            this.editToolStripMenuItem.Size = new System.Drawing.Size(47, 24);
            this.editToolStripMenuItem.Text = "Edit";
            // 
            // tridentToolStripMenuItem
            // 
            this.tridentToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.onToolStripMenuItem,
            this.offToolStripMenuItem});
            this.tridentToolStripMenuItem.Name = "tridentToolStripMenuItem";
            this.tridentToolStripMenuItem.Size = new System.Drawing.Size(167, 24);
            this.tridentToolStripMenuItem.Text = "Trident";
            // 
            // onToolStripMenuItem
            // 
            this.onToolStripMenuItem.Name = "onToolStripMenuItem";
            this.onToolStripMenuItem.Size = new System.Drawing.Size(99, 24);
            this.onToolStripMenuItem.Text = "On";
            this.onToolStripMenuItem.Click += new System.EventHandler(this.onToolStripMenuItem_Click);
            // 
            // offToolStripMenuItem
            // 
            this.offToolStripMenuItem.Name = "offToolStripMenuItem";
            this.offToolStripMenuItem.Size = new System.Drawing.Size(99, 24);
            this.offToolStripMenuItem.Text = "Off";
            this.offToolStripMenuItem.Click += new System.EventHandler(this.offToolStripMenuItem_Click);
            // 
            // gridToolStripMenuItem
            // 
            this.gridToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.onToolStripMenuItem1,
            this.offToolStripMenuItem1});
            this.gridToolStripMenuItem.Name = "gridToolStripMenuItem";
            this.gridToolStripMenuItem.Size = new System.Drawing.Size(167, 24);
            this.gridToolStripMenuItem.Text = "Grid";
            // 
            // onToolStripMenuItem1
            // 
            this.onToolStripMenuItem1.Name = "onToolStripMenuItem1";
            this.onToolStripMenuItem1.Size = new System.Drawing.Size(99, 24);
            this.onToolStripMenuItem1.Text = "On";
            this.onToolStripMenuItem1.Click += new System.EventHandler(this.onToolStripMenuItem1_Click);
            // 
            // offToolStripMenuItem1
            // 
            this.offToolStripMenuItem1.Name = "offToolStripMenuItem1";
            this.offToolStripMenuItem1.Size = new System.Drawing.Size(99, 24);
            this.offToolStripMenuItem1.Text = "Off";
            this.offToolStripMenuItem1.Click += new System.EventHandler(this.offToolStripMenuItem1_Click);
            // 
            // modeToolStripMenuItem
            // 
            this.modeToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.wireframeToolStripMenuItem,
            this.solidToolStripMenuItem,
            this.pointToolStripMenuItem});
            this.modeToolStripMenuItem.Name = "modeToolStripMenuItem";
            this.modeToolStripMenuItem.Size = new System.Drawing.Size(167, 24);
            this.modeToolStripMenuItem.Text = "Mode";
            // 
            // wireframeToolStripMenuItem
            // 
            this.wireframeToolStripMenuItem.Name = "wireframeToolStripMenuItem";
            this.wireframeToolStripMenuItem.Size = new System.Drawing.Size(148, 24);
            this.wireframeToolStripMenuItem.Text = "Wireframe";
            this.wireframeToolStripMenuItem.Click += new System.EventHandler(this.wireframeToolStripMenuItem_Click);
            // 
            // solidToolStripMenuItem
            // 
            this.solidToolStripMenuItem.Name = "solidToolStripMenuItem";
            this.solidToolStripMenuItem.Size = new System.Drawing.Size(148, 24);
            this.solidToolStripMenuItem.Text = "Fill";
            this.solidToolStripMenuItem.Click += new System.EventHandler(this.solidToolStripMenuItem_Click);
            // 
            // pointToolStripMenuItem
            // 
            this.pointToolStripMenuItem.Name = "pointToolStripMenuItem";
            this.pointToolStripMenuItem.Size = new System.Drawing.Size(148, 24);
            this.pointToolStripMenuItem.Text = "Point";
            this.pointToolStripMenuItem.Click += new System.EventHandler(this.pointToolStripMenuItem_Click);
            // 
            // boundingBoxToolStripMenuItem
            // 
            this.boundingBoxToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.onToolStripMenuItem2,
            this.offToolStripMenuItem2});
            this.boundingBoxToolStripMenuItem.Name = "boundingBoxToolStripMenuItem";
            this.boundingBoxToolStripMenuItem.Size = new System.Drawing.Size(167, 24);
            this.boundingBoxToolStripMenuItem.Text = "BoundingBox";
            // 
            // onToolStripMenuItem2
            // 
            this.onToolStripMenuItem2.Name = "onToolStripMenuItem2";
            this.onToolStripMenuItem2.Size = new System.Drawing.Size(99, 24);
            this.onToolStripMenuItem2.Text = "On";
            this.onToolStripMenuItem2.Click += new System.EventHandler(this.onToolStripMenuItem2_Click);
            // 
            // offToolStripMenuItem2
            // 
            this.offToolStripMenuItem2.Name = "offToolStripMenuItem2";
            this.offToolStripMenuItem2.Size = new System.Drawing.Size(99, 24);
            this.offToolStripMenuItem2.Text = "Off";
            this.offToolStripMenuItem2.Click += new System.EventHandler(this.offToolStripMenuItem2_Click);
            // 
            // helpToolStripMenuItem
            // 
            this.helpToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.tridentToolStripMenuItem1});
            this.helpToolStripMenuItem.Name = "helpToolStripMenuItem";
            this.helpToolStripMenuItem.Size = new System.Drawing.Size(53, 24);
            this.helpToolStripMenuItem.Text = "Help";
            // 
            // tridentToolStripMenuItem1
            // 
            this.tridentToolStripMenuItem1.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.xToolStripMenuItem,
            this.yGreenToolStripMenuItem,
            this.zRedToolStripMenuItem});
            this.tridentToolStripMenuItem1.Name = "tridentToolStripMenuItem1";
            this.tridentToolStripMenuItem1.Size = new System.Drawing.Size(125, 24);
            this.tridentToolStripMenuItem1.Text = "Trident";
            // 
            // xToolStripMenuItem
            // 
            this.xToolStripMenuItem.Name = "xToolStripMenuItem";
            this.xToolStripMenuItem.Size = new System.Drawing.Size(139, 24);
            this.xToolStripMenuItem.Text = "X - Blue";
            // 
            // yGreenToolStripMenuItem
            // 
            this.yGreenToolStripMenuItem.Name = "yGreenToolStripMenuItem";
            this.yGreenToolStripMenuItem.Size = new System.Drawing.Size(139, 24);
            this.yGreenToolStripMenuItem.Text = "Y - Green";
            // 
            // zRedToolStripMenuItem
            // 
            this.zRedToolStripMenuItem.Name = "zRedToolStripMenuItem";
            this.zRedToolStripMenuItem.Size = new System.Drawing.Size(139, 24);
            this.zRedToolStripMenuItem.Text = "Z - Red";
            // 
            // uiOpenFile
            // 
            this.uiOpenFile.FileName = "openFileDialog1";
            // 
            // statusStrip1
            // 
            this.statusStrip1.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.uiFpsText,
            this.uiFpsLabel,
            this.uiFileText,
            this.uiFileLabel,
            this.uiModelText,
            this.uiModeLabel,
            this.uiCamPosText,
            this.uiCamPosLabel,
            this.uiTargetPosText,
            this.uiTargetPosLabel});
            this.statusStrip1.Location = new System.Drawing.Point(0, 528);
            this.statusStrip1.Name = "statusStrip1";
            this.statusStrip1.Size = new System.Drawing.Size(782, 25);
            this.statusStrip1.TabIndex = 1;
            this.statusStrip1.Text = "statusStrip1";
            // 
            // uiFpsText
            // 
            this.uiFpsText.Name = "uiFpsText";
            this.uiFpsText.Size = new System.Drawing.Size(34, 20);
            this.uiFpsText.Text = "Fps:";
            // 
            // uiFpsLabel
            // 
            this.uiFpsLabel.Name = "uiFpsLabel";
            this.uiFpsLabel.Size = new System.Drawing.Size(15, 20);
            this.uiFpsLabel.Text = "-";
            // 
            // uiFileText
            // 
            this.uiFileText.Name = "uiFileText";
            this.uiFileText.Size = new System.Drawing.Size(35, 20);
            this.uiFileText.Text = "File:";
            // 
            // uiFileLabel
            // 
            this.uiFileLabel.Name = "uiFileLabel";
            this.uiFileLabel.Size = new System.Drawing.Size(15, 20);
            this.uiFileLabel.Text = "-";
            // 
            // uiModelText
            // 
            this.uiModelText.Name = "uiModelText";
            this.uiModelText.Size = new System.Drawing.Size(51, 20);
            this.uiModelText.Text = "Mode:";
            // 
            // uiModeLabel
            // 
            this.uiModeLabel.Name = "uiModeLabel";
            this.uiModeLabel.Size = new System.Drawing.Size(44, 20);
            this.uiModeLabel.Text = "Trans";
            // 
            // uiCamPosText
            // 
            this.uiCamPosText.Name = "uiCamPosText";
            this.uiCamPosText.Size = new System.Drawing.Size(65, 20);
            this.uiCamPosText.Text = "CamPos:";
            // 
            // uiCamPosLabel
            // 
            this.uiCamPosLabel.Name = "uiCamPosLabel";
            this.uiCamPosLabel.Size = new System.Drawing.Size(15, 20);
            this.uiCamPosLabel.Text = "-";
            // 
            // uiTargetPosText
            // 
            this.uiTargetPosText.Name = "uiTargetPosText";
            this.uiTargetPosText.Size = new System.Drawing.Size(78, 20);
            this.uiTargetPosText.Text = "TargetPos:";
            // 
            // uiTargetPosLabel
            // 
            this.uiTargetPosLabel.Name = "uiTargetPosLabel";
            this.uiTargetPosLabel.Size = new System.Drawing.Size(15, 20);
            this.uiTargetPosLabel.Text = "-";
            // 
            // contextMenuStrip1
            // 
            this.contextMenuStrip1.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.translateToolStripMenuItem,
            this.rotateToolStripMenuItem,
            this.toolStripSeparator2,
            this.resetTargetPosToolStripMenuItem});
            this.contextMenuStrip1.Name = "contextMenuStrip1";
            this.contextMenuStrip1.Size = new System.Drawing.Size(181, 82);
            // 
            // translateToolStripMenuItem
            // 
            this.translateToolStripMenuItem.Name = "translateToolStripMenuItem";
            this.translateToolStripMenuItem.Size = new System.Drawing.Size(180, 24);
            this.translateToolStripMenuItem.Text = "Translate";
            this.translateToolStripMenuItem.Click += new System.EventHandler(this.translateToolStripMenuItem_Click);
            // 
            // rotateToolStripMenuItem
            // 
            this.rotateToolStripMenuItem.Name = "rotateToolStripMenuItem";
            this.rotateToolStripMenuItem.Size = new System.Drawing.Size(180, 24);
            this.rotateToolStripMenuItem.Text = "Rotate";
            this.rotateToolStripMenuItem.Click += new System.EventHandler(this.rotateToolStripMenuItem_Click);
            // 
            // toolStripSeparator2
            // 
            this.toolStripSeparator2.Name = "toolStripSeparator2";
            this.toolStripSeparator2.Size = new System.Drawing.Size(177, 6);
            // 
            // resetTargetPosToolStripMenuItem
            // 
            this.resetTargetPosToolStripMenuItem.Name = "resetTargetPosToolStripMenuItem";
            this.resetTargetPosToolStripMenuItem.Size = new System.Drawing.Size(180, 24);
            this.resetTargetPosToolStripMenuItem.Text = "ResetTargetPos";
            this.resetTargetPosToolStripMenuItem.Click += new System.EventHandler(this.resetTargetPosToolStripMenuItem_Click);
            // 
            // uiTab
            // 
            this.uiTab.Controls.Add(this.uiTabInfo);
            this.uiTab.Controls.Add(this.uiTabProperty);
            this.uiTab.Location = new System.Drawing.Point(571, 31);
            this.uiTab.Name = "uiTab";
            this.uiTab.SelectedIndex = 0;
            this.uiTab.Size = new System.Drawing.Size(200, 494);
            this.uiTab.TabIndex = 3;
            // 
            // uiTabInfo
            // 
            this.uiTabInfo.Controls.Add(this.uiInfoLabel);
            this.uiTabInfo.Location = new System.Drawing.Point(4, 25);
            this.uiTabInfo.Name = "uiTabInfo";
            this.uiTabInfo.Padding = new System.Windows.Forms.Padding(3);
            this.uiTabInfo.Size = new System.Drawing.Size(192, 465);
            this.uiTabInfo.TabIndex = 1;
            this.uiTabInfo.Text = "Info";
            this.uiTabInfo.UseVisualStyleBackColor = true;
            // 
            // uiInfoLabel
            // 
            this.uiInfoLabel.Dock = System.Windows.Forms.DockStyle.Fill;
            this.uiInfoLabel.Location = new System.Drawing.Point(3, 3);
            this.uiInfoLabel.Name = "uiInfoLabel";
            this.uiInfoLabel.Size = new System.Drawing.Size(186, 459);
            this.uiInfoLabel.TabIndex = 0;
            this.uiInfoLabel.Text = "";
            // 
            // uiTabProperty
            // 
            this.uiTabProperty.Location = new System.Drawing.Point(4, 25);
            this.uiTabProperty.Name = "uiTabProperty";
            this.uiTabProperty.Padding = new System.Windows.Forms.Padding(3);
            this.uiTabProperty.Size = new System.Drawing.Size(192, 465);
            this.uiTabProperty.TabIndex = 0;
            this.uiTabProperty.Text = "Property";
            this.uiTabProperty.UseVisualStyleBackColor = true;
            // 
            // uiSplitContainer
            // 
            this.uiSplitContainer.Location = new System.Drawing.Point(12, 31);
            this.uiSplitContainer.Name = "uiSplitContainer";
            this.uiSplitContainer.Orientation = System.Windows.Forms.Orientation.Horizontal;
            // 
            // uiSplitContainer.Panel1
            // 
            this.uiSplitContainer.Panel1.Controls.Add(this.uiSplitTop);
            // 
            // uiSplitContainer.Panel2
            // 
            this.uiSplitContainer.Panel2.Controls.Add(this.uiSplitBottom);
            this.uiSplitContainer.Size = new System.Drawing.Size(552, 490);
            this.uiSplitContainer.SplitterDistance = 182;
            this.uiSplitContainer.TabIndex = 4;
            // 
            // uiSplitTop
            // 
            this.uiSplitTop.Dock = System.Windows.Forms.DockStyle.Fill;
            this.uiSplitTop.Location = new System.Drawing.Point(0, 0);
            this.uiSplitTop.Name = "uiSplitTop";
            // 
            // uiSplitTop.Panel1
            // 
            this.uiSplitTop.Panel1.Controls.Add(this.uiViewIndicator1);
            this.uiSplitTop.Panel1.Controls.Add(this.uiGlControlTopLeft);
            // 
            // uiSplitTop.Panel2
            // 
            this.uiSplitTop.Panel2.Controls.Add(this.uiViewIndicator2);
            this.uiSplitTop.Panel2.Controls.Add(this.uiGlControlTopRight);
            this.uiSplitTop.Size = new System.Drawing.Size(552, 182);
            this.uiSplitTop.SplitterDistance = 244;
            this.uiSplitTop.TabIndex = 0;
            this.uiSplitTop.SplitterMoved += new System.Windows.Forms.SplitterEventHandler(this.uiSplitTop_SplitterMoved);
            // 
            // uiViewIndicator1
            // 
            this.uiViewIndicator1.AutoSize = true;
            this.uiViewIndicator1.Location = new System.Drawing.Point(0, 0);
            this.uiViewIndicator1.Name = "uiViewIndicator1";
            this.uiViewIndicator1.Size = new System.Drawing.Size(33, 17);
            this.uiViewIndicator1.TabIndex = 1;
            this.uiViewIndicator1.Text = "Top";
            // 
            // uiGlControlTopLeft
            // 
            this.uiGlControlTopLeft.AutoSize = true;
            this.uiGlControlTopLeft.AutoSizeMode = System.Windows.Forms.AutoSizeMode.GrowAndShrink;
            this.uiGlControlTopLeft.BackColor = System.Drawing.Color.Black;
            this.uiGlControlTopLeft.ContextMenuStrip = this.contextMenuStrip1;
            this.uiGlControlTopLeft.Dock = System.Windows.Forms.DockStyle.Fill;
            this.uiGlControlTopLeft.Location = new System.Drawing.Point(0, 0);
            this.uiGlControlTopLeft.Margin = new System.Windows.Forms.Padding(4, 4, 4, 4);
            this.uiGlControlTopLeft.Name = "uiGlControlTopLeft";
            this.uiGlControlTopLeft.Size = new System.Drawing.Size(244, 182);
            this.uiGlControlTopLeft.TabIndex = 0;
            this.uiGlControlTopLeft.VSync = false;
            this.uiGlControlTopLeft.Load += new System.EventHandler(this.TopViewPanelLoad);
            this.uiGlControlTopLeft.MouseLeave += new System.EventHandler(this.GLControlLeave);
            this.uiGlControlTopLeft.Paint += new System.Windows.Forms.PaintEventHandler(this.GLControlPaint);
            this.uiGlControlTopLeft.MouseDown += new System.Windows.Forms.MouseEventHandler(this.ControlMouseDown);
            this.uiGlControlTopLeft.Resize += new System.EventHandler(this.GLControlResize);
            this.uiGlControlTopLeft.MouseUp += new System.Windows.Forms.MouseEventHandler(this.ControlMouseUp);
            this.uiGlControlTopLeft.MouseEnter += new System.EventHandler(this.GLControlEnter);
            // 
            // uiViewIndicator2
            // 
            this.uiViewIndicator2.AutoSize = true;
            this.uiViewIndicator2.Location = new System.Drawing.Point(0, 0);
            this.uiViewIndicator2.Name = "uiViewIndicator2";
            this.uiViewIndicator2.Size = new System.Drawing.Size(41, 17);
            this.uiViewIndicator2.TabIndex = 2;
            this.uiViewIndicator2.Text = "Right";
            // 
            // uiGlControlTopRight
            // 
            this.uiGlControlTopRight.AutoSize = true;
            this.uiGlControlTopRight.AutoSizeMode = System.Windows.Forms.AutoSizeMode.GrowAndShrink;
            this.uiGlControlTopRight.BackColor = System.Drawing.Color.Black;
            this.uiGlControlTopRight.ContextMenuStrip = this.contextMenuStrip1;
            this.uiGlControlTopRight.Dock = System.Windows.Forms.DockStyle.Fill;
            this.uiGlControlTopRight.Location = new System.Drawing.Point(0, 0);
            this.uiGlControlTopRight.Margin = new System.Windows.Forms.Padding(4, 4, 4, 4);
            this.uiGlControlTopRight.Name = "uiGlControlTopRight";
            this.uiGlControlTopRight.Size = new System.Drawing.Size(304, 182);
            this.uiGlControlTopRight.TabIndex = 0;
            this.uiGlControlTopRight.VSync = false;
            this.uiGlControlTopRight.Load += new System.EventHandler(this.RightViewPanelLoad);
            this.uiGlControlTopRight.MouseLeave += new System.EventHandler(this.GLControlLeave);
            this.uiGlControlTopRight.Paint += new System.Windows.Forms.PaintEventHandler(this.GLControlPaint);
            this.uiGlControlTopRight.MouseDown += new System.Windows.Forms.MouseEventHandler(this.ControlMouseDown);
            this.uiGlControlTopRight.Resize += new System.EventHandler(this.GLControlResize);
            this.uiGlControlTopRight.MouseUp += new System.Windows.Forms.MouseEventHandler(this.ControlMouseUp);
            this.uiGlControlTopRight.MouseEnter += new System.EventHandler(this.GLControlEnter);
            // 
            // uiSplitBottom
            // 
            this.uiSplitBottom.Dock = System.Windows.Forms.DockStyle.Fill;
            this.uiSplitBottom.Location = new System.Drawing.Point(0, 0);
            this.uiSplitBottom.Name = "uiSplitBottom";
            // 
            // uiSplitBottom.Panel1
            // 
            this.uiSplitBottom.Panel1.Controls.Add(this.uiViewIndicator3);
            this.uiSplitBottom.Panel1.Controls.Add(this.uiGlControlBottomLeft);
            // 
            // uiSplitBottom.Panel2
            // 
            this.uiSplitBottom.Panel2.Controls.Add(this.uiViewIndicator4);
            this.uiSplitBottom.Panel2.Controls.Add(this.uiGlControlBottomRight);
            this.uiSplitBottom.Size = new System.Drawing.Size(552, 304);
            this.uiSplitBottom.SplitterDistance = 244;
            this.uiSplitBottom.TabIndex = 0;
            this.uiSplitBottom.SplitterMoved += new System.Windows.Forms.SplitterEventHandler(this.uiSplitTop_SplitterMoved);
            // 
            // uiViewIndicator3
            // 
            this.uiViewIndicator3.AutoSize = true;
            this.uiViewIndicator3.Location = new System.Drawing.Point(0, 0);
            this.uiViewIndicator3.Name = "uiViewIndicator3";
            this.uiViewIndicator3.Size = new System.Drawing.Size(41, 17);
            this.uiViewIndicator3.TabIndex = 2;
            this.uiViewIndicator3.Text = "Front";
            // 
            // uiGlControlBottomLeft
            // 
            this.uiGlControlBottomLeft.AutoSize = true;
            this.uiGlControlBottomLeft.AutoSizeMode = System.Windows.Forms.AutoSizeMode.GrowAndShrink;
            this.uiGlControlBottomLeft.BackColor = System.Drawing.Color.Black;
            this.uiGlControlBottomLeft.ContextMenuStrip = this.contextMenuStrip1;
            this.uiGlControlBottomLeft.Dock = System.Windows.Forms.DockStyle.Fill;
            this.uiGlControlBottomLeft.Location = new System.Drawing.Point(0, 0);
            this.uiGlControlBottomLeft.Margin = new System.Windows.Forms.Padding(4, 4, 4, 4);
            this.uiGlControlBottomLeft.Name = "uiGlControlBottomLeft";
            this.uiGlControlBottomLeft.Size = new System.Drawing.Size(244, 304);
            this.uiGlControlBottomLeft.TabIndex = 0;
            this.uiGlControlBottomLeft.VSync = false;
            this.uiGlControlBottomLeft.Load += new System.EventHandler(this.FrontViewPanelLoad);
            this.uiGlControlBottomLeft.MouseLeave += new System.EventHandler(this.GLControlLeave);
            this.uiGlControlBottomLeft.Paint += new System.Windows.Forms.PaintEventHandler(this.GLControlPaint);
            this.uiGlControlBottomLeft.MouseDown += new System.Windows.Forms.MouseEventHandler(this.ControlMouseDown);
            this.uiGlControlBottomLeft.Resize += new System.EventHandler(this.GLControlResize);
            this.uiGlControlBottomLeft.MouseUp += new System.Windows.Forms.MouseEventHandler(this.ControlMouseUp);
            this.uiGlControlBottomLeft.MouseEnter += new System.EventHandler(this.GLControlEnter);
            // 
            // uiViewIndicator4
            // 
            this.uiViewIndicator4.AutoSize = true;
            this.uiViewIndicator4.Location = new System.Drawing.Point(0, 0);
            this.uiViewIndicator4.Name = "uiViewIndicator4";
            this.uiViewIndicator4.Size = new System.Drawing.Size(82, 17);
            this.uiViewIndicator4.TabIndex = 3;
            this.uiViewIndicator4.Text = "Perspective";
            // 
            // uiGlControlBottomRight
            // 
            this.uiGlControlBottomRight.AutoSize = true;
            this.uiGlControlBottomRight.AutoSizeMode = System.Windows.Forms.AutoSizeMode.GrowAndShrink;
            this.uiGlControlBottomRight.BackColor = System.Drawing.Color.Black;
            this.uiGlControlBottomRight.ContextMenuStrip = this.contextMenuStrip1;
            this.uiGlControlBottomRight.Dock = System.Windows.Forms.DockStyle.Fill;
            this.uiGlControlBottomRight.Location = new System.Drawing.Point(0, 0);
            this.uiGlControlBottomRight.Margin = new System.Windows.Forms.Padding(4, 4, 4, 4);
            this.uiGlControlBottomRight.Name = "uiGlControlBottomRight";
            this.uiGlControlBottomRight.Size = new System.Drawing.Size(304, 304);
            this.uiGlControlBottomRight.TabIndex = 0;
            this.uiGlControlBottomRight.VSync = false;
            this.uiGlControlBottomRight.Load += new System.EventHandler(this.PerspectivePanelLoad);
            this.uiGlControlBottomRight.MouseLeave += new System.EventHandler(this.GLControlLeave);
            this.uiGlControlBottomRight.Paint += new System.Windows.Forms.PaintEventHandler(this.GLControlPaint);
            this.uiGlControlBottomRight.MouseDown += new System.Windows.Forms.MouseEventHandler(this.ControlMouseDown);
            this.uiGlControlBottomRight.Resize += new System.EventHandler(this.GLControlResize);
            this.uiGlControlBottomRight.MouseUp += new System.Windows.Forms.MouseEventHandler(this.ControlMouseUp);
            this.uiGlControlBottomRight.MouseEnter += new System.EventHandler(this.GLControlEnter);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(782, 553);
            this.Controls.Add(this.statusStrip1);
            this.Controls.Add(this.uiMenuStrip);
            this.Controls.Add(this.uiSplitContainer);
            this.Controls.Add(this.uiTab);
            this.MainMenuStrip = this.uiMenuStrip;
            this.Name = "Form1";
            this.Text = "SimpleCFDModelViewer";
            this.Resize += new System.EventHandler(this.Form1_Resize);
            this.uiMenuStrip.ResumeLayout(false);
            this.uiMenuStrip.PerformLayout();
            this.statusStrip1.ResumeLayout(false);
            this.statusStrip1.PerformLayout();
            this.contextMenuStrip1.ResumeLayout(false);
            this.uiTab.ResumeLayout(false);
            this.uiTabInfo.ResumeLayout(false);
            this.uiSplitContainer.Panel1.ResumeLayout(false);
            this.uiSplitContainer.Panel2.ResumeLayout(false);
            this.uiSplitContainer.ResumeLayout(false);
            this.uiSplitTop.Panel1.ResumeLayout(false);
            this.uiSplitTop.Panel1.PerformLayout();
            this.uiSplitTop.Panel2.ResumeLayout(false);
            this.uiSplitTop.Panel2.PerformLayout();
            this.uiSplitTop.ResumeLayout(false);
            this.uiSplitBottom.Panel1.ResumeLayout(false);
            this.uiSplitBottom.Panel1.PerformLayout();
            this.uiSplitBottom.Panel2.ResumeLayout(false);
            this.uiSplitBottom.Panel2.PerformLayout();
            this.uiSplitBottom.ResumeLayout(false);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.MenuStrip uiMenuStrip;
        private System.Windows.Forms.ToolStripMenuItem fileToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem openToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem saveToolStripMenuItem;
        private System.Windows.Forms.ToolStripSeparator toolStripSeparator1;
        private System.Windows.Forms.ToolStripMenuItem exitToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem editToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem helpToolStripMenuItem;
        private System.Windows.Forms.OpenFileDialog uiOpenFile;
        private System.Windows.Forms.SaveFileDialog uiSaveFile;
        private System.Windows.Forms.StatusStrip statusStrip1;
        private System.Windows.Forms.ToolStripStatusLabel uiFpsLabel;
        private System.Windows.Forms.ToolStripStatusLabel uiFileText;
        private System.Windows.Forms.ToolStripStatusLabel uiFileLabel;
        private System.Windows.Forms.ContextMenuStrip contextMenuStrip1;
        private System.Windows.Forms.ToolStripMenuItem translateToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem rotateToolStripMenuItem;
        private System.Windows.Forms.ToolStripSeparator toolStripSeparator2;
        private System.Windows.Forms.TabControl uiTab;
        private System.Windows.Forms.TabPage uiTabProperty;
        private System.Windows.Forms.TabPage uiTabInfo;
        private System.Windows.Forms.SplitContainer uiSplitContainer;
        private System.Windows.Forms.SplitContainer uiSplitTop;
        private System.Windows.Forms.SplitContainer uiSplitBottom;
        private OpenTK.GLControl uiGlControlTopLeft;
        private OpenTK.GLControl uiGlControlTopRight;
        private OpenTK.GLControl uiGlControlBottomLeft;
        private OpenTK.GLControl uiGlControlBottomRight;
        private System.Windows.Forms.RichTextBox uiInfoLabel;
        private System.Windows.Forms.ToolStripStatusLabel uiModelText;
        private System.Windows.Forms.ToolStripStatusLabel uiModeLabel;
        private System.Windows.Forms.ToolStripStatusLabel uiCamPosText;
        private System.Windows.Forms.ToolStripStatusLabel uiCamPosLabel;
        private System.Windows.Forms.ToolStripStatusLabel uiTargetPosText;
        private System.Windows.Forms.ToolStripStatusLabel uiTargetPosLabel;
        private System.Windows.Forms.ToolStripMenuItem resetTargetPosToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem tridentToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem onToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem offToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem gridToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem onToolStripMenuItem1;
        private System.Windows.Forms.ToolStripMenuItem offToolStripMenuItem1;
        private System.Windows.Forms.Label uiViewIndicator1;
        private System.Windows.Forms.Label uiViewIndicator2;
        private System.Windows.Forms.Label uiViewIndicator4;
        private System.Windows.Forms.Label uiViewIndicator3;
        private System.Windows.Forms.ToolStripMenuItem modeToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem wireframeToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem solidToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem pointToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem tridentToolStripMenuItem1;
        private System.Windows.Forms.ToolStripMenuItem xToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem yGreenToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem zRedToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem boundingBoxToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem onToolStripMenuItem2;
        private System.Windows.Forms.ToolStripMenuItem offToolStripMenuItem2;
        private System.Windows.Forms.ToolStripStatusLabel uiFpsText;


    }
}

