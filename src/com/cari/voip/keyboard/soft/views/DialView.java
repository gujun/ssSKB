package com.cari.voip.keyboard.soft.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

import com.cari.voip.keyboard.soft.Activator;
import com.cari.voip.keyboard.soft.image.MenuImage;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersSession;

public class DialView extends ViewPart implements PropertyChangeListener {

	public static final String ID_VIEW = 
		"com.cari.voip.keyboard.soft.views.DialView";
	
	private Combo numCombo;
	private Label call;
	
	public DialView() {
		super();
		
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite comp= new Composite(parent,SWT.NONE);
		comp.setLayout(new GridLayout(1,true));
		
		Composite compup= new Group(comp, SWT.NONE);
		compup.setLayout(new GridLayout(1,true));
		compup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		// combo
		CoolBar coolbar = new CoolBar(compup, SWT.NONE);
		coolbar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		CoolItem item = new CoolItem(coolbar, SWT.NONE);
		item.setControl(createComboView(coolbar,new GridData(
				GridData.FILL_HORIZONTAL)));
		calcSize(item);
		
		
		
		Composite compCmd= new Composite(compup,SWT.NONE);
		compCmd.setLayout(new GridLayout(3,true));
		compCmd.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		//reset
		Label reset = new Label(compCmd,SWT.PUSH);
		//reset.setText("«Âø’");
		reset.setImage(MenuImage.reset);
		reset.setToolTipText("«Âø’À˘ ‰∫≈¬Î");
		reset.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		reset.setCursor(new Cursor(parent.getDisplay(),SWT.CURSOR_HAND));
		reset.addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent e){
				
			}
			public void mouseDown(MouseEvent e){
				
			}
			public void mouseUp(MouseEvent e){
				numCombo.setText("");
			}
		});
		
		Label ok = new Label(compCmd,SWT.PUSH);
		//reset.setText("«Âø’");
		//ok.setImage(MenuImage.reset);
		//ok.setToolTipText("«Âø’À˘ ‰∫≈¬Î");
		
		ok.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
	    call = new Label(compCmd,SWT.PUSH);
		//call.setText("∫ÙΩ–");
		call.setImage(MenuImage.dial);
		call.setToolTipText("∫ÙΩ–…œ ˆ∫≈¬Î");
		call.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		call.setCursor(new Cursor(parent.getDisplay(),SWT.CURSOR_HAND));
		call.addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent e){
				
			}
			public void mouseDown(MouseEvent e){
				
			}
			public void mouseUp(MouseEvent e){
				String number = numCombo.getText();
				if(number == null || number.length() == 0){
					return;
				}
				SwitchUsersSession session = Activator.getSwitchUsersSession();
				if(session != null){
					session.input(SwitchUsersSession.INPUT_TYPE_DIAL, number);
				}
				if(numCombo.indexOf(number) == -1){
					numCombo.add(number);
				}
			}
		});
		
		
		Composite compNum= new Composite(comp, SWT.NONE);
		compNum.setLayout(new GridLayout(3,true));
		compNum.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		
		
		Label num1 = new Label(compNum,SWT.PUSH);
		//star.setText("*");
		num1.setImage(MenuImage.num1);
		num1.setToolTipText("1");
		num1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		num1.setCursor(new Cursor(parent.getDisplay(),SWT.CURSOR_HAND));
		num1.addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent e){
				
			}
			public void mouseDown(MouseEvent e){
				
			}
			public void mouseUp(MouseEvent e){
				String old = numCombo.getText();
				numCombo.setText((old==null)?"1":old+"1");
			}
		});
		
		Label num2 = new Label(compNum,SWT.PUSH);
		//star.setText("*");
		num2.setImage(MenuImage.num2);
		num2.setToolTipText("2");
		num2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		num2.setCursor(new Cursor(parent.getDisplay(),SWT.CURSOR_HAND));
		num2.addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent e){
				
			}
			public void mouseDown(MouseEvent e){
				
			}
			public void mouseUp(MouseEvent e){
				String old = numCombo.getText();
				numCombo.setText((old==null)?"2":old+"2");
			}
		});
		Label num3 = new Label(compNum,SWT.PUSH);
		//star.setText("*");
		num3.setImage(MenuImage.num3);
		num3.setToolTipText("3");
		num3.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		num3.setCursor(new Cursor(parent.getDisplay(),SWT.CURSOR_HAND));
		num3.addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent e){
				
			}
			public void mouseDown(MouseEvent e){
				
			}
			public void mouseUp(MouseEvent e){
				String old = numCombo.getText();
				numCombo.setText((old==null)?"3":old+"3");
			}
		});
		
		Label num4 = new Label(compNum,SWT.PUSH);
		//star.setText("*");
		num4.setImage(MenuImage.num4);
		num4.setToolTipText("4");
		num4.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		num4.setCursor(new Cursor(parent.getDisplay(),SWT.CURSOR_HAND));
		num4.addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent e){
				
			}
			public void mouseDown(MouseEvent e){
				
			}
			public void mouseUp(MouseEvent e){
				String old = numCombo.getText();
				numCombo.setText((old==null)?"4":old+"4");
			}
		});
		Label num5 = new Label(compNum,SWT.PUSH);
		//star.setText("*");
		num5.setImage(MenuImage.num5);
		num5.setToolTipText("5");
		num5.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		num5.setCursor(new Cursor(parent.getDisplay(),SWT.CURSOR_HAND));
		num5.addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent e){
				
			}
			public void mouseDown(MouseEvent e){
				
			}
			public void mouseUp(MouseEvent e){
				String old = numCombo.getText();
				numCombo.setText((old==null)?"5":old+"5");
			}
		});
		Label num6 = new Label(compNum,SWT.PUSH);
		//star.setText("*");
		num6.setImage(MenuImage.num6);
		num6.setToolTipText("6");
		num6.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		num6.setCursor(new Cursor(parent.getDisplay(),SWT.CURSOR_HAND));
		num6.addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent e){
				
			}
			public void mouseDown(MouseEvent e){
				
			}
			public void mouseUp(MouseEvent e){
				String old = numCombo.getText();
				numCombo.setText((old==null)?"6":old+"6");
			}
		});
		Label num7 = new Label(compNum,SWT.PUSH);
		//star.setText("*");
		num7.setImage(MenuImage.num7);
		num7.setToolTipText("7");
		num7.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		num7.setCursor(new Cursor(parent.getDisplay(),SWT.CURSOR_HAND));
		num7.addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent e){
				
			}
			public void mouseDown(MouseEvent e){
				
			}
			public void mouseUp(MouseEvent e){
				String old = numCombo.getText();
				numCombo.setText((old==null)?"7":old+"7");
			}
		});
		Label num8 = new Label(compNum,SWT.PUSH);
		//star.setText("*");
		num8.setImage(MenuImage.num8);
		num8.setToolTipText("8");
		num8.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		num8.setCursor(new Cursor(parent.getDisplay(),SWT.CURSOR_HAND));
		num8.addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent e){
				
			}
			public void mouseDown(MouseEvent e){
				
			}
			public void mouseUp(MouseEvent e){
				String old = numCombo.getText();
				numCombo.setText((old==null)?"8":old+"8");
			}
		});
		Label num9 = new Label(compNum,SWT.PUSH);
		//star.setText("*");
		num9.setImage(MenuImage.num9);
		num9.setToolTipText("9");
		num9.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		num9.setCursor(new Cursor(parent.getDisplay(),SWT.CURSOR_HAND));
		num9.addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent e){
				
			}
			public void mouseDown(MouseEvent e){
				
			}
			public void mouseUp(MouseEvent e){
				String old = numCombo.getText();
				numCombo.setText((old==null)?"9":old+"9");
			}
		});
		
		
		Label star = new Label(compNum,SWT.PUSH);
		//star.setText("*");
		star.setImage(MenuImage.num10);
		star.setToolTipText("*");
		star.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		star.setCursor(new Cursor(parent.getDisplay(),SWT.CURSOR_HAND));
		star.addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent e){
				
			}
			public void mouseDown(MouseEvent e){
				
			}
			public void mouseUp(MouseEvent e){
				String old = numCombo.getText();
				numCombo.setText((old==null)?"*":old+"*");
			}
		});
		Label zero = new Label(compNum,SWT.PUSH);
		//zero.setText("0");
		zero.setImage(MenuImage.num0);
		zero.setToolTipText("0");
		zero.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		zero.setCursor(new Cursor(parent.getDisplay(),SWT.CURSOR_HAND));
		zero.addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent e){
				
			}
			public void mouseDown(MouseEvent e){
				
			}
			public void mouseUp(MouseEvent e){
				String old = numCombo.getText();
				numCombo.setText((old==null)?"0":old+"0");
			}
		});
		Label wall = new Label(compNum,SWT.PUSH);
		//wall.setText("#");
		wall.setImage(MenuImage.num11);
		wall.setToolTipText("#");
		wall.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		wall.setCursor(new Cursor(parent.getDisplay(),SWT.CURSOR_HAND));
		wall.addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent e){
				
			}
			public void mouseDown(MouseEvent e){
				
			}
			public void mouseUp(MouseEvent e){
				String old = numCombo.getText();
				numCombo.setText((old==null)?"#":old+"#");
			}
		});
		addOnlineLister();
	}
	
	private Control createComboView(Composite parent, Object layoutData) {
		Font numFont=new Font(parent.getDisplay(),"ÀŒÃÂ",18,SWT.BOLD ); 
		numCombo = new Combo(parent, SWT.NONE);
		numCombo.setFont(numFont);
		numCombo.setLayoutData(layoutData);
		numCombo.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e){
				//final String num = ((Combo) e.getSource()).getText();
				
				//numCombo.add(num);
			}
			
			public void widgetSelected(SelectionEvent e){
				//final String num = ((Combo) e.getSource()).getText();
				
				//numCombo.add(num);
			}
		});
		return numCombo;
	}
	private void calcSize(CoolItem item){
		Control control = item.getControl();
		Point pt = control.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		pt = item.computeSize(pt.x, pt.y);
		item.setSize(pt);
	}
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		if(this.numCombo != null){
			this.numCombo.setFocus();
		}
		
	}
	public void dispose() {
		
		 super.dispose();
		 removeOnlineLister();
	 }

	protected void addOnlineLister(){
		SwitchUsersSession session = Activator.getSwitchUsersSession();
		if(session != null){
			session.addPropertyChangeListener(SwitchUsersSession.PROP_DISPATCHER_ONLINE,this);
			this.call.setEnabled(session.canDail());
		}
	}
	protected void removeOnlineLister(){
		SwitchUsersSession session = Activator.getSwitchUsersSession();
		if(session != null){
			session.removePropertyChangeListener(SwitchUsersSession.PROP_DISPATCHER_ONLINE,this);
		}
	}
	protected void checkEnable(Object value){
		/*boolean v = true;
		if(value instanceof Boolean){
			Boolean val = (Boolean)value;
			 v = val.booleanValue();
			
			
		}
		else if(value instanceof String){
			String val = (String)value;
			if(val.equals("end") || val.equals("none")){
				v = false;
			}
		}
		this.call.setEnabled(v);*/
		SwitchUsersSession session = Activator.getSwitchUsersSession();
		if(session != null){
			this.call.setEnabled(session.canDail());
		}
		
		
		
	}
	@Override
	public void propertyChange(final PropertyChangeEvent evt) {
		Activator.getDisplay().asyncExec(new Runnable() {
		      public void run() {
		    	  if(evt.getPropertyName().equals(SwitchUsersSession.PROP_DISPATCHER_ONLINE)){
		    		  checkEnable(evt.getNewValue());
		    	  }
		    	 
			}
		   });
	}
}
