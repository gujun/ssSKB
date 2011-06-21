package com.cari.voip.keyboard.soft.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;

import com.cari.voip.keyboard.soft.editors.GatewayEditor;
import com.cari.voip.keyboard.soft.editors.LocalEditor;
import com.cari.voip.keyboard.soft.editors.input.GatewayInput;

public class OpenAction extends Action implements ActionFactory.IWorkbenchAction {
	private final IWorkbenchWindow window;
	
	static int index = 0;
	
	public OpenAction(IWorkbenchWindow window) {
		this.window = window;
		this.setId("org.eclipse.jface.action.Action.OpenAction");
	}
	
	public void dispose() {
		
	}
	
	public void run(){
		IWorkbenchPage page = window.getActivePage();
		String id = null;
		
		if((index%2) == 0){
			id = GatewayEditor.ID;
		}
		else{
			id = LocalEditor.ID;
		}
		index++;
		
		IEditorPart editor = null;
		
		IEditorInput input = new GatewayInput();
		
		try{
			editor = page.openEditor(input, id);
			
		}catch(Exception e){
			if(editor != null){
				editor.dispose();
			}
			e.printStackTrace();
			
		}
	}


}
