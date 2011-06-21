package com.cari.voip.keyboard.soft.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.cari.voip.keyboard.soft.Activator;
import com.cari.voip.keyboard.soft.adapter.callFailContentProvider;
import com.cari.voip.keyboard.soft.adapter.callFailLabelProvider;
import com.cari.voip.keyboard.soft.adapter.dtContentProvider;
import com.cari.voip.keyboard.soft.adapter.dtLabelProvider;
import com.cari.voip.keyboard.soft.adapter.periodContentProvider;
import com.cari.voip.keyboard.soft.adapter.periodLabelProvider;
import com.cari.voip.keyboard.soft.image.MenuImage;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUser;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersGroup;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersManager;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersSession;
import com.cari.voip.keyboard.soft.views.shell.userShell;

public class dispatDtDialog extends Dialog {
	public String m_dt;
	public String m_dt_en;
	public String m_dt_p;
	



	
	private Button dt_choice_en;
	private Button dt_choice_dis;
	
	private TabFolder tabFolder;
	
	private TableViewer dtTV;
	private Table dtTb;
	
	private Combo dtType;
	private Combo dtNum;
	private Composite bottom;
	//private Text T_dt;
	private MouseListener T_dt_MouseListener;
	private IDoubleClickListener  userShellDclick;
	private boolean T_dt_MouseListener_set =false;
	
	private TableViewer timeTV;
	private Table timeTb;
	
	private Combo starth;
	private Combo startm;
	private Combo endh;
	private Combo endm;
	
	private Display displayDevice;
	
	public dispatDtDialog(Shell parent,String dt_en){
		this(parent,dt_en,null,null);
	}
	public dispatDtDialog(Shell parent,String dt_en,String dt,String dt_p){
		super(parent);
		this.m_dt_en = dt_en;
		if(this.m_dt_en != null){
			this.m_dt_en = this.m_dt_en.toLowerCase();
		}
		this.m_dt = dt;
		this.m_dt_p = dt_p;
	}
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("夜服设置");
	}
	protected void setButtonLayoutData(Button button) {
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_END);
		
		button.setLayoutData(data);
	}
	protected void createButtonsForButtonBar(Composite parent) {
		// create OK and Cancel buttons by default
		createButton(parent, IDialogConstants.OK_ID, "  设  置  ",
				true);//IDialogConstants.CANCEL_LABEL
		createButton(parent, IDialogConstants.CANCEL_ID,
				"  取  消  ", false);//IDialogConstants.CANCEL_LABEL
	}
	protected Control createDialogArea(Composite root) {
		// create a composite with standard margins and spacing
		displayDevice = root.getDisplay();
		Composite parent = new Composite(root, SWT.NONE);//SWT.NONE);
		//parent.setText("夜服功能");
		GridLayout toplayout = new GridLayout(1,true);
		toplayout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		toplayout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		toplayout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		toplayout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		parent.setLayout(toplayout);
		parent.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		
		
	
		//RadioGroupFieldEditor rdGroup = new RadioGroupFieldEditor("dt_en","夜服功能",2,new String[][]{{"启用","true"},{"禁用","false"}},composite,true);
		//rdGroup.setPreferenceName("dt_en");
		/*Label enPre = new Label(composite,SWT.NONE);
		enPre.setText("enable：");
		enPre.setLayoutData(new GridData(SWT.END,SWT.CENTER,false,false));
		
		T_dt_en  = new Text(composite,SWT.SINGLE | SWT.BORDER);
		//keyLabel.setTextLimit(36);
		T_dt_en.setText(this.m_dt_en);
		GridData keyGridData = new GridData(SWT.FILL,SWT.FILL,false,false);
		keyGridData.widthHint = 200;
		T_dt_en.setLayoutData(keyGridData);
		*/
		tabFolder = new TabFolder(parent,SWT.NONE);
		
		
		Composite composite = new Composite(tabFolder,SWT.NONE);
		composite.setLayoutData(new GridData(SWT.CENTER,SWT.CENTER,false,false));
		createDTArea(composite);
		TabItem numTab = new TabItem(tabFolder,SWT.NONE);
		numTab.setText("夜服号码");
		numTab.setControl(composite);
		
		
		
		Composite composite2 = new Composite(tabFolder,SWT.NONE);
		composite2.setLayoutData(new GridData(SWT.CENTER,SWT.CENTER,false,false));
		createTimeArea(composite2);
		TabItem timeTab = new TabItem(tabFolder,SWT.NONE);
		timeTab.setText("夜服时间段");
		timeTab.setControl(composite2);
		
		Composite header = new Composite(parent,SWT.NONE);
		
		GridLayout headerlayout = new GridLayout(3,true);
		headerlayout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		headerlayout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		headerlayout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		headerlayout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		header.setLayout(headerlayout);
		header.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label enPre = new Label(header,SWT.NONE);
		enPre.setText("夜服功能：");
		enPre.setLayoutData(new GridData(SWT.END,SWT.CENTER,false,false));
		
		//Button[] rds = n Button[2]; 
		dt_choice_en = new Button(header,SWT.RADIO);
		dt_choice_en.setText("启用");
		dt_choice_en.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,false));
		
		dt_choice_dis = new Button(header,SWT.RADIO);
		dt_choice_dis.setText("禁用");
		dt_choice_dis.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,false));
		
		
		//T_dt_en.setFocus();
		dt_choice_en.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				tabFolder.setEnabled(true);
			}
			
		});
		dt_choice_dis.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				tabFolder.setEnabled(false);
				
			}
			
		});
		updateDisplay();
		
		
		
		return parent;
	}
	protected Control createButtonBar(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		// create a layout with spacing and margins appropriate for the font
		// size.
		GridLayout layout = new GridLayout(3,true);
		layout.numColumns = 0; // this is incremented by createButton
		layout.makeColumnsEqualWidth = true;
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		composite.setLayout(layout);
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_END
				| GridData.VERTICAL_ALIGN_CENTER);
		composite.setLayoutData(data);
		composite.setFont(parent.getFont());
		
		// Add the buttons to the button bar.
		createButtonsForButtonBar(composite);
		return composite;
	}
	protected void refreshdt_en(){
		if(this.m_dt_en!=null && this.m_dt_en.equals("true")){
			dt_choice_en.setSelection(true);
			dt_choice_dis.setSelection(false);
			
		}else{
			dt_choice_en.setSelection(false);
			dt_choice_dis.setSelection(true);
			
		}
	}
	protected void refreshdt(){
		this.dtTV.setInput(this.m_dt);
		this.dtTV.refresh();
		this.dtTb.setTopIndex(this.dtTb.getItemCount()-1);
	}
	protected void refreshdt_p(){
		this.timeTV.setInput(this.m_dt_p);
		this.timeTV.refresh();
		this.timeTb.setTopIndex(this.timeTb.getItemCount()-1);
	}
	protected void updateDisplay(){
		
		refreshdt_en();
		refreshdt();
		refreshdt_p();
		
	}
	protected void createDTArea(Composite composite)
	{
		GridLayout layout = new GridLayout(1,false);
		//layout.marginHeight = convertVerticalDLUsToPixels(2*IDialogConstants.VERTICAL_MARGIN);
		//layout.marginWidth = convertHorizontalDLUsToPixels(2*IDialogConstants.HORIZONTAL_MARGIN);
		//layout.verticalSpacing = convertVerticalDLUsToPixels(2*IDialogConstants.VERTICAL_SPACING);
		//layout.horizontalSpacing = convertHorizontalDLUsToPixels(2*IDialogConstants.HORIZONTAL_SPACING);
		composite.setLayout(layout);
		
		Composite top = new Composite(composite,SWT.NONE);
		GridLayout toplayout = new GridLayout(2,false);
		//toplayout.marginHeight = convertVerticalDLUsToPixels(2*IDialogConstants.VERTICAL_MARGIN);
		//toplayout.marginWidth = convertHorizontalDLUsToPixels(2*IDialogConstants.HORIZONTAL_MARGIN);
		//toplayout.verticalSpacing = convertVerticalDLUsToPixels(2*IDialogConstants.VERTICAL_SPACING);
		//toplayout.horizontalSpacing = convertHorizontalDLUsToPixels(2*IDialogConstants.HORIZONTAL_SPACING);
		top.setLayout(toplayout);
		top.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		
		
		Composite left = new Composite(top,SWT.NONE);
		GridLayout leftlayout = new GridLayout(1,false);
		//leftlayout.marginHeight = convertVerticalDLUsToPixels(2*IDialogConstants.VERTICAL_MARGIN);
		//leftlayout.marginWidth = convertHorizontalDLUsToPixels(2*IDialogConstants.HORIZONTAL_MARGIN);
		//leftlayout.verticalSpacing = convertVerticalDLUsToPixels(2*IDialogConstants.VERTICAL_SPACING);
		//leftlayout.horizontalSpacing = convertHorizontalDLUsToPixels(2*IDialogConstants.HORIZONTAL_SPACING);
		left.setLayout(leftlayout);
		left.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		
		this.dtTV = new TableViewer(left,SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		this.dtTb = this.dtTV.getTable();
		this.dtTb.setLayoutData(new GridData(GridData.FILL_BOTH));
		this.dtTb.setHeaderVisible(true);
		this.dtTb.setLinesVisible(true);
		TableLayout tblayout = new TableLayout();
		this.dtTb.setLayout(tblayout);
		
		
		tblayout.addColumnData(new ColumnWeightData(120));
		new TableColumn(this.dtTb,SWT.NONE).setText("类型");
		
		tblayout.addColumnData(new ColumnWeightData(120));
		new TableColumn(this.dtTb,SWT.NONE).setText("号码");
		
		this.dtTV.setLabelProvider(new dtLabelProvider());
		this.dtTV.setContentProvider(new dtContentProvider());
		
		Composite right = new Composite(top,SWT.NONE);
		GridLayout rightlayout = new GridLayout(1,false);
		//rightlayout.marginHeight = convertVerticalDLUsToPixels(2*IDialogConstants.VERTICAL_MARGIN);
		//rightlayout.marginWidth = convertHorizontalDLUsToPixels(2*IDialogConstants.HORIZONTAL_MARGIN);
		//rightlayout.verticalSpacing = convertVerticalDLUsToPixels(2*IDialogConstants.VERTICAL_SPACING);
		//rightlayout.horizontalSpacing = convertHorizontalDLUsToPixels(2*IDialogConstants.HORIZONTAL_SPACING);
		right.setLayout(rightlayout);
		right.setLayoutData(new GridData(SWT.LEFT,SWT.TOP,false,false));
		
		Button up = new Button(right,SWT.NONE);
		up.setImage(MenuImage.up);
		up.setToolTipText("上移");
		up.addMouseListener(new MouseListener(){

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseUp(MouseEvent e) {
				IStructuredSelection selection = (IStructuredSelection)dtTV.getSelection();
				Object o = selection.getFirstElement();
				if(o != null && o instanceof String && m_dt != null){
					String p = (String)o;
					String[] ps = m_dt.split("[,|]");
					int len = ps.length;
					int k = 0;
					int add = 0;
					for(int i = 0;i<len;i++){
						if(p.equals(ps[i])){
							
							k = i;
						}
					}
					if(k > 0 && k <len){
						String t = ps[k];
						ps[k] = ps[k-1];
						ps[k-1] = t;
						String newp = "";
						for(int i = 0;i<len;i++){
							if(ps[i] == null || ps[i].length() ==0){
								continue;
							}
							if(add ==0){
								newp = newp.concat(ps[i]);
								add++;
							}else{
								newp = newp.concat(","+ps[i]);
								add++;
							}
						
						}
						m_dt = newp;
						refreshdt();
						dtTb.setTopIndex(k-1);
					}
					
				}
				
			}
			
		});
		Button down = new Button(right,SWT.NONE);
		down.setImage(MenuImage.down);
		down.setToolTipText("下移");
		down.addMouseListener(new MouseListener(){

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseUp(MouseEvent e) {

				IStructuredSelection selection = (IStructuredSelection)dtTV.getSelection();
				Object o = selection.getFirstElement();
				if(o != null && o instanceof String && m_dt != null){
					String p = (String)o;
					String[] ps = m_dt.split("[,|]");
					int len = ps.length;
					int k = 0;
					int add = 0;
					for(int i = 0;i<len;i++){
						if(p.equals(ps[i])){
							
							k = i;
						}
					}
					if(k >= 0 && k <len-1){
						String t = ps[k];
						ps[k] = ps[k+1];
						ps[k+1] = t;
						String newp = "";
						for(int i = 0;i<len;i++){
							if(ps[i] == null || ps[i].length() ==0){
								continue;
							}
							if(add ==0){
								newp = newp.concat(ps[i]);
								add++;
							}else{
								newp = newp.concat(","+ps[i]);
								add++;
							}
						
						}
						m_dt = newp;
						refreshdt();
						dtTb.setTopIndex(k-1);
					}
					
				}
				
			
				
			}
			
		});
		Button rm = new Button(right,SWT.NONE);
		rm.setImage(MenuImage.rm);
		rm.setToolTipText("删除该项");
		rm.addMouseListener(new MouseListener(){

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseUp(MouseEvent e) {

				IStructuredSelection selection = (IStructuredSelection)dtTV.getSelection();
				Object o = selection.getFirstElement();
				if(o != null && o instanceof String && m_dt != null){
					String p = (String)o;
					String[] ps = m_dt.split("[,|]");
					int len = ps.length;
					int k = 0;
					int add = 0;
					for(int i = 0;i<len;i++){
						if(p.equals(ps[i])){
							
							k = i;
						}
					}
					if(k >= 0 && k <len){
						String t = ps[k];
						ps[k] = null;
						String newp = "";
						for(int i = 0;i<len;i++){
							if(ps[i] == null || ps[i].length() ==0){
								continue;
							}
							if(add ==0){
								newp = newp.concat(ps[i]);
								add++;
							}else{
								newp = newp.concat(","+ps[i]);
								add++;
							}
						
						}
						m_dt = newp;
						refreshdt();
						
						dtTb.setTopIndex((k-1)<=0?0:k-1);
					}
					
				}
				
			
				
			}
			
		});
		Button del = new Button(right,SWT.NONE);
		del.setImage(MenuImage.del);
		del.setToolTipText("删除所有项");
		del.addMouseListener(new MouseListener(){

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseUp(MouseEvent e) {
				if(m_dt == null || m_dt.length() == 0){
					return;
				}
				m_dt = "";
				refreshdt();
			}
			
		});
		
		bottom = new Composite(composite,SWT.NONE);
		GridLayout bottomlayout = new GridLayout(8,false);
		//bottomlayout.marginHeight = convertVerticalDLUsToPixels(2*IDialogConstants.VERTICAL_MARGIN);
		//bottomlayout.marginWidth = convertHorizontalDLUsToPixels(2*IDialogConstants.HORIZONTAL_MARGIN);
		//bottomlayout.verticalSpacing = convertVerticalDLUsToPixels(2*IDialogConstants.VERTICAL_SPACING);
		//bottomlayout.horizontalSpacing = convertHorizontalDLUsToPixels(2*IDialogConstants.HORIZONTAL_SPACING);
		bottom.setLayout(bottomlayout);
		bottom.setLayoutData(new GridData(SWT.FILL,SWT.BOTTOM,false,false));
		
		//GridData typeGridData = new GridData(SWT.LEFT,SWT.CENTER,false,false);
		//typeGridData.widthHint = 60;
		
		 dtType = new Combo(bottom, SWT.READ_ONLY);
		fillDtType(dtType);
		dtType.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				Object o = e.getSource();
				if(dtNum != null && o != null && o instanceof Combo ){
					Combo c = (Combo)o;
					String sel = c.getText();
					dtNum.setText("");
					if(sel != null && sel.startsWith("本地")){
						fillDt(dtNum,sel);
						//setUserShell();
					}else{
						dtNum.removeAll();
						//clearUserShell();
					}
				}
			}
			
		});
		
		//dtType.setLayoutData(typeGridData);
		
		Label starthm = new Label(bottom,SWT.NONE);
		starthm.setText(":");
		dtNum = new Combo(bottom, SWT.NONE);
		//fillMn(dtGroup);
		GridData numGridData = new GridData(SWT.FILL,SWT.CENTER,false,false);
		numGridData.widthHint = 120;
		dtNum.setLayoutData(numGridData);
		/*
		//Label dt_pPre = new Label(bottom,SWT.NONE);
		//dt_pPre.setText("--");
		
		T_dt = new Text(composite,SWT.SINGLE | SWT.BORDER);
		//this.RegText.setTextLimit(36);
		//if(this.value != null){
		//	T_dt.setText(this.m_dt);
		//}
		GridData dtGridData = new GridData(SWT.FILL,SWT.FILL,false,false);
		dtGridData.widthHint = 60;
		T_dt.setLayoutData(dtGridData);
		*/
		Button add = new Button(bottom,SWT.NONE);
		add.setImage(MenuImage.ad);
		add.setText("增加该号码");
		add.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,false,false));
		add.addMouseListener(new MouseListener(){

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseUp(MouseEvent e) {
				String t = dtType.getText();
				String g = "";
				if(t == null || t.length() == 0){
					dtType.setFocus();
					return;
				}
				if(t.startsWith("本地")){
					t = "internal";
				}else if(t.startsWith("网关("))
				{
					
					g = t.substring("网关(".length());
					if(g == null){
						dtType.setFocus();
						return;
					}
					if(g.endsWith(")")){
						g = g.substring(0, g.length()-1);
					}
					if(g == null || g.length()==0){
						dtType.setFocus();
						return;
					}
					t= "gateway";
				}else{
					dtType.setFocus();
					return;
				}
				String n = dtNum.getText();//T_dt.getText();
				if(n == null || n.length() == 0){
					dtNum.setFocus();//T_dt.setFocus();
					return;
				}
				String[] ns = n.split("[^0-9]");
				if(ns == null || ns[0] == null || ns[0].length() == 0){
					dtNum.setFocus();//T_dt.setFocus();
					return;
				}
				n = ns[0];
				String p = "sofia/";
				if(t.equals("internal")){
					p = p.concat(t+"/"+n);
				}else 
				{
					p = p.concat(t+"/"+g+"/"+n);
				}
				
				if(m_dt == null || m_dt.length() == 0){
					m_dt = p;
				}else{
					m_dt = m_dt.concat(","+p);
				}
				refreshdt();
			
			}
			
		});
		/*GridLayout layout = new GridLayout(1,false);
		//layout.marginHeight = convertVerticalDLUsToPixels(2*IDialogConstants.VERTICAL_MARGIN);
		//layout.marginWidth = convertHorizontalDLUsToPixels(2*IDialogConstants.HORIZONTAL_MARGIN);
		//layout.verticalSpacing = convertVerticalDLUsToPixels(2*IDialogConstants.VERTICAL_SPACING);
		//layout.horizontalSpacing = convertHorizontalDLUsToPixels(2*IDialogConstants.HORIZONTAL_SPACING);
		composite.setLayout(layout);
		//applyDialogFont(composite);
		
		Composite top = new Composite(composite,SWT.NONE);
		GridLayout toplayout = new GridLayout(2,false);
		top.setLayout(toplayout);
		top.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		
		
		Label dtPre = new Label(composite,SWT.NONE);
		dtPre.setText("夜服号码：");
		dtPre.setLayoutData(new GridData(SWT.END,SWT.CENTER,false,false));
		
		T_dt = new Text(composite,SWT.SINGLE | SWT.BORDER);
		//this.RegText.setTextLimit(36);
		//if(this.value != null){
			T_dt.setText(this.m_dt);
		//}
		GridData dtGridData = new GridData(SWT.FILL,SWT.FILL,false,false);
		dtGridData.widthHint = 200;
		T_dt.setLayoutData(dtGridData);
		*/
	}
	protected String getDT(){
		
		return this.m_dt;
	}
	protected void createTimeArea(Composite composite)
	{
		
		GridLayout layout = new GridLayout(1,false);
		//layout.marginHeight = convertVerticalDLUsToPixels(2*IDialogConstants.VERTICAL_MARGIN);
		//layout.marginWidth = convertHorizontalDLUsToPixels(2*IDialogConstants.HORIZONTAL_MARGIN);
		//layout.verticalSpacing = convertVerticalDLUsToPixels(2*IDialogConstants.VERTICAL_SPACING);
		//layout.horizontalSpacing = convertHorizontalDLUsToPixels(2*IDialogConstants.HORIZONTAL_SPACING);
		composite.setLayout(layout);
		
		Composite top = new Composite(composite,SWT.NONE);
		GridLayout toplayout = new GridLayout(2,false);
		//toplayout.marginHeight = convertVerticalDLUsToPixels(2*IDialogConstants.VERTICAL_MARGIN);
		//toplayout.marginWidth = convertHorizontalDLUsToPixels(2*IDialogConstants.HORIZONTAL_MARGIN);
		//toplayout.verticalSpacing = convertVerticalDLUsToPixels(2*IDialogConstants.VERTICAL_SPACING);
		//toplayout.horizontalSpacing = convertHorizontalDLUsToPixels(2*IDialogConstants.HORIZONTAL_SPACING);
		top.setLayout(toplayout);
		top.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		
		
		Composite left = new Composite(top,SWT.NONE);
		GridLayout leftlayout = new GridLayout(1,false);
		//leftlayout.marginHeight = convertVerticalDLUsToPixels(2*IDialogConstants.VERTICAL_MARGIN);
		//leftlayout.marginWidth = convertHorizontalDLUsToPixels(2*IDialogConstants.HORIZONTAL_MARGIN);
		//leftlayout.verticalSpacing = convertVerticalDLUsToPixels(2*IDialogConstants.VERTICAL_SPACING);
		//leftlayout.horizontalSpacing = convertHorizontalDLUsToPixels(2*IDialogConstants.HORIZONTAL_SPACING);
		left.setLayout(leftlayout);
		left.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		
		this.timeTV = new TableViewer(left,SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		this.timeTb = this.timeTV.getTable();
		this.timeTb.setLayoutData(new GridData(GridData.FILL_BOTH));
		this.timeTb.setHeaderVisible(true);
		this.timeTb.setLinesVisible(true);
		TableLayout tblayout = new TableLayout();
		this.timeTb.setLayout(tblayout);
		
		
		tblayout.addColumnData(new ColumnWeightData(120));
		new TableColumn(this.timeTb,SWT.NONE).setText("开始时刻");
		
		tblayout.addColumnData(new ColumnWeightData(120));
		new TableColumn(this.timeTb,SWT.NONE).setText("结束时刻");
		
		this.timeTV.setLabelProvider(new periodLabelProvider());
		this.timeTV.setContentProvider(new periodContentProvider());
		
		Composite right = new Composite(top,SWT.NONE);
		GridLayout rightlayout = new GridLayout(1,false);
		//rightlayout.marginHeight = convertVerticalDLUsToPixels(2*IDialogConstants.VERTICAL_MARGIN);
		//rightlayout.marginWidth = convertHorizontalDLUsToPixels(2*IDialogConstants.HORIZONTAL_MARGIN);
		//rightlayout.verticalSpacing = convertVerticalDLUsToPixels(2*IDialogConstants.VERTICAL_SPACING);
		//rightlayout.horizontalSpacing = convertHorizontalDLUsToPixels(2*IDialogConstants.HORIZONTAL_SPACING);
		right.setLayout(rightlayout);
		right.setLayoutData(new GridData(SWT.LEFT,SWT.TOP,false,false));
		
		Button up = new Button(right,SWT.NONE);
		up.setImage(MenuImage.up);
		up.setToolTipText("上移");
		up.addMouseListener(new MouseListener(){

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseUp(MouseEvent e) {
				IStructuredSelection selection = (IStructuredSelection)timeTV.getSelection();
				Object o = selection.getFirstElement();
				if(o != null && o instanceof String && m_dt_p != null){
					String p = (String)o;
					String[] ps = m_dt_p.split(",");
					int len = ps.length;
					int k = 0;
					int add = 0;
					for(int i = 0;i<len;i++){
						if(p.equals(ps[i])){
							
							k = i;
						}
					}
					if(k > 0 && k <len){
						String t = ps[k];
						ps[k] = ps[k-1];
						ps[k-1] = t;
						String newp = "";
						for(int i = 0;i<len;i++){
							if(ps[i] == null || ps[i].length() ==0){
								continue;
							}
							if(add ==0){
								newp = newp.concat(ps[i]);
								add++;
							}else{
								newp = newp.concat(","+ps[i]);
								add++;
							}
						
						}
						m_dt_p = newp;
						refreshdt_p();
						timeTb.setTopIndex(k-1);
					}
					
				}
				
			}
			
		});
		Button down = new Button(right,SWT.NONE);
		down.setImage(MenuImage.down);
		down.setToolTipText("下移");
		down.addMouseListener(new MouseListener(){

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseUp(MouseEvent e) {

				IStructuredSelection selection = (IStructuredSelection)timeTV.getSelection();
				Object o = selection.getFirstElement();
				if(o != null && o instanceof String && m_dt_p != null){
					String p = (String)o;
					String[] ps = m_dt_p.split(",");
					int len = ps.length;
					int k = 0;
					int add = 0;
					for(int i = 0;i<len;i++){
						if(p.equals(ps[i])){
							
							k = i;
						}
					}
					if(k >= 0 && k <len-1){
						String t = ps[k];
						ps[k] = ps[k+1];
						ps[k+1] = t;
						String newp = "";
						for(int i = 0;i<len;i++){
							if(ps[i] == null || ps[i].length() ==0){
								continue;
							}
							if(add ==0){
								newp = newp.concat(ps[i]);
								add++;
							}else{
								newp = newp.concat(","+ps[i]);
								add++;
							}
						
						}
						m_dt_p = newp;
						refreshdt_p();
						timeTb.setTopIndex(k-1);
					}
					
				}
				
			
				
			}
			
		});
		Button rm = new Button(right,SWT.NONE);
		rm.setImage(MenuImage.rm);
		rm.setToolTipText("删除该项");
		rm.addMouseListener(new MouseListener(){

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseUp(MouseEvent e) {

				IStructuredSelection selection = (IStructuredSelection)timeTV.getSelection();
				Object o = selection.getFirstElement();
				if(o != null && o instanceof String && m_dt_p != null){
					String p = (String)o;
					String[] ps = m_dt_p.split(",");
					int len = ps.length;
					int k = 0;
					int add = 0;
					for(int i = 0;i<len;i++){
						if(p.equals(ps[i])){
							
							k = i;
						}
					}
					if(k >= 0 && k <len){
						String t = ps[k];
						ps[k] = null;
						String newp = "";
						for(int i = 0;i<len;i++){
							if(ps[i] == null || ps[i].length() ==0){
								continue;
							}
							if(add ==0){
								newp = newp.concat(ps[i]);
								add++;
							}else{
								newp = newp.concat(","+ps[i]);
								add++;
							}
						
						}
						m_dt_p = newp;
						refreshdt_p();
						
						timeTb.setTopIndex((k-1)<=0?0:k-1);
					}
					
				}
				
			
				
			}
			
		});
		Button del = new Button(right,SWT.NONE);
		del.setImage(MenuImage.del);
		del.setToolTipText("删除所有项");
		del.addMouseListener(new MouseListener(){

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseUp(MouseEvent e) {
				if(m_dt_p == null || m_dt_p.length() == 0){
					return;
				}
				m_dt_p = "";
				refreshdt_p();
			}
			
		});
		Composite bottom = new Composite(composite,SWT.NONE);
		GridLayout bottomlayout = new GridLayout(8,false);
		//bottomlayout.marginHeight = convertVerticalDLUsToPixels(2*IDialogConstants.VERTICAL_MARGIN);
		//bottomlayout.marginWidth = convertHorizontalDLUsToPixels(2*IDialogConstants.HORIZONTAL_MARGIN);
		//bottomlayout.verticalSpacing = convertVerticalDLUsToPixels(2*IDialogConstants.VERTICAL_SPACING);
		//bottomlayout.horizontalSpacing = convertHorizontalDLUsToPixels(2*IDialogConstants.HORIZONTAL_SPACING);
		bottom.setLayout(bottomlayout);
		bottom.setLayoutData(new GridData(SWT.FILL,SWT.BOTTOM,false,false));
		
		GridData cmbGridData = new GridData(SWT.LEFT,SWT.CENTER,false,false);
		cmbGridData.widthHint = 20;
		
		 starth = new Combo(bottom, SWT.READ_ONLY);
		fillHr(starth);
		starth.setLayoutData(cmbGridData);
		
		Label starthm = new Label(bottom,SWT.NONE);
		starthm.setText(":");
		 startm = new Combo(bottom, SWT.READ_ONLY);
		fillMn(startm);
		startm.setLayoutData(cmbGridData);
		
		Label dt_pPre = new Label(bottom,SWT.NONE);
		dt_pPre.setText("--");
		//dt_pPre.setLayoutData(new GridData(SWT.END,SWT.CENTER,false,false));
		
		 endh = new Combo(bottom, SWT.READ_ONLY);
		fillHr(endh);
		endh.setLayoutData(cmbGridData);
		
		Label endhm = new Label(bottom,SWT.NONE);
		endhm.setText(":");
		
		 endm = new Combo(bottom, SWT.READ_ONLY);
		fillMn(endm);
		endm.setLayoutData(cmbGridData);
		
		/*T_dt_p = new Text(bottom,SWT.SINGLE | SWT.BORDER);
		//this.RegText.setTextLimit(36);
		//if(this.value != null){
		T_dt_p.setText(this.m_dt_p);
		//}
		
		GridData dtpGridData = new GridData(SWT.FILL,SWT.FILL,false,false);
		dtpGridData.widthHint = 200;
		T_dt_p.setLayoutData(dtpGridData);*/
		Button add = new Button(bottom,SWT.NONE);
		add.setImage(MenuImage.ad);
		add.setText("增加该时间段");
		add.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,false,false));
		add.addMouseListener(new MouseListener(){

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseUp(MouseEvent e) {
				String sh = starth.getText();
				if(sh == null || sh.length() == 0){
					starth.setFocus();
					return;
				}
				String sm = startm.getText();
				if(sm == null || sm.length() == 0){
					startm.setFocus();
					return;
				}
				String eh = endh.getText();
				if(eh == null || eh.length() == 0){
					endh.setFocus();
					return;
				}
				String em = endm.getText();
				if(em == null || em.length() == 0){
					endm.setFocus();
					return;
				}
				String p = sh+":"+sm+"-"+eh+":"+em;
				if(m_dt_p == null || m_dt_p.length() == 0){
					m_dt_p = p;
				}else{
					m_dt_p = m_dt_p.concat(","+p);
				}
				refreshdt_p();
			
			}
			
		});
	}
	protected void fillDtType(Combo t){
		t.removeAll();
		SwitchUsersSession session = Activator.getSwitchUsersSession();
		t.add("本地");
		if(session != null){
			SwitchUsersManager local = session.getlocalUserManager();
			if(local != null){
				SwitchUsersGroup[] localGroups = local.toGroupsArray();
				if(localGroups != null && localGroups.length > 1){
					for(int i = 1;i<localGroups.length;i++){
						String gname = localGroups[i].getName();
						if(gname != null && gname.length() > 0){
							t.add("本地["+gname+"]");
						}
					}
				}
			}
			SwitchUsersManager gw = session.getgatewayUserManager();
			if(gw != null){
				SwitchUsersGroup[] gwGroups = gw.toGroupsArray();
				if(gwGroups != null && gwGroups.length > 1){
					for(int i = 1;i<gwGroups.length;i++){
						String gname = gwGroups[i].getName();
						if(gname != null && gname.length() > 0){
							t.add("网关("+gname+")");
						}
					}
				}
			}
		}
		
		//t.add("本地（组1）");
		//t.add("网关(tomx8o)");
		t.pack();
	}
	protected void fillDt(Combo t,String sel){
		t.removeAll();
		SwitchUsersSession session = Activator.getSwitchUsersSession();
		if(session != null && sel != null){
			if(sel.equals("本地")){
				SwitchUsersManager local = session.getlocalUserManager();
				if(local != null){
					SwitchUsersGroup member = local.getMembersGroup();
					if(member != null){
						SwitchUser[] users = (SwitchUser[]) member.getSwitchUsersArray();
						for(int i = 0;i<users.length;i++){
							String uid = users[i].getUserId();
							String desc = users[i].getDesc();
							if(desc != null && desc.length() > 8){
								desc = desc.substring(0, 7);
								desc = desc.concat("..");
							}
							if(uid != null && uid.length() > 0){
								if(desc != null && desc.length() > 0){
									t.add(uid+"["+desc+"]");
								}else{
									t.add(uid);
								}
							}
							
						}
					}
				}
			}else if(sel.startsWith("本地[")){
				String gname = sel.substring("本地[".length());
				if(gname != null && gname.endsWith("]")){
					gname = gname.substring(0,gname.length()-1);
				}
				if(gname != null && gname.length() > 0){
				SwitchUsersManager local = session.getlocalUserManager();
				if(local != null){
					SwitchUsersGroup member = local.getSwitchUsersGroup(gname);
					if(member != null){
						SwitchUser[] users = (SwitchUser[]) member.getSwitchUsersArray();
						for(int i = 0;i<users.length;i++){
							String uid = users[i].getUserId();
							String desc = users[i].getDesc();
							if(desc != null && desc.length() > 8){
								desc = desc.substring(0, 7);
								desc = desc.concat("..");
							}
							if(uid != null && uid.length() > 0){
								if(desc != null && desc.length() > 0){
									t.add(uid+"["+desc+"]");
								}else{
									t.add(uid);
								}
							}
						}
					}
				}
				}
			
			}
		}
	}
/*
	protected void setUserShell(){
		if(userShellDclick == null){
			userShellDclick = new IDoubleClickListener(){

				@Override
				public void doubleClick(DoubleClickEvent event) {
					ISelection selection = event.getSelection();
					if(selection instanceof IStructuredSelection){
						Object obj = ((IStructuredSelection)selection).getFirstElement();
						
						if(obj instanceof SwitchUser){
							if(T_dt != null){
								
								T_dt.setText(((SwitchUser)obj).getUserId());
								
								
							}
						}//obj
					}//selection
				}
				
			};
		}
		if(T_dt_MouseListener == null){
			T_dt_MouseListener = new MouseListener(){

				@Override
				public void mouseDoubleClick(MouseEvent e) {
					
					Rectangle dateRect= displayDevice.map(bottom,null,T_dt.getBounds());
					//Rectangle calRect= userShell.getBounds();
					
					userShell.setVisible(displayDevice,dateRect, userShellDclick);
					
				}

				@Override
				public void mouseDown(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseUp(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
			};
		}
		T_dt.setToolTipText("双击弹出用户列表");
		T_dt.addMouseListener(T_dt_MouseListener);
		T_dt_MouseListener_set = true;
	}
	protected void clearUserShell(){
		T_dt.setToolTipText("");
		if(T_dt_MouseListener_set && T_dt_MouseListener != null){
			T_dt.removeMouseListener(T_dt_MouseListener);
			T_dt_MouseListener_set = false;
		};
		
	}
	*/
	protected void fillHr(Combo hr){
		hr.removeAll();
		for(int i = 0;i<24;i++){
			hr.add(String.valueOf(i));
		}
	}
	protected void fillMn(Combo mn){
		mn.removeAll();
		for(int i = 0;i<60;i++){
			mn.add(String.valueOf(i));
		}
	}
	protected String getTime(){
		return this.m_dt_p;
	}
	protected void okPressed() {
		
		if(this.dt_choice_en.getSelection()){
			this.m_dt_en = "true";
		}else{
			this.m_dt_en = "false";
		}
		
		//this.m_dt_en = this.T_dt_en.getText();
		
		//this.m_dt = this.T_dt.getText();
		
		//this.m_dt_p = this.T_dt_p.getText();
		

		super.okPressed();
	}
	
}
