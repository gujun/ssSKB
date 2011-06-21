package com.cari.voip.keyboard.soft.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.DeleteAction;
import org.eclipse.gef.ui.actions.PrintAction;
import org.eclipse.gef.ui.actions.RedoAction;
import org.eclipse.gef.ui.actions.SaveAction;
import org.eclipse.gef.ui.actions.SelectAllAction;
import org.eclipse.gef.ui.actions.UndoAction;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;

import com.cari.voip.keyboard.soft.editors.edit.SwitchUserEditPartFactory;
import com.cari.voip.keyboard.soft.editors.graphicalView.SwitchUserGraphicalViewer;
import com.cari.voip.keyboard.soft.editors.input.SwitchUserInput;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersGroup;

public class SwitchUserGraphicalEditor extends GraphicalEditor {

	public static final String ID = 
		"com.cari.voip.keyboard.soft.editors.SwitchUserGraphicalEditor";
	
	private SwitchUsersGroup diagram;

	
	public SwitchUserGraphicalEditor(){
		setEditDomain(new DefaultEditDomain(this));
		
	}
	
	protected void closeEditor(boolean save) {
		getSite().getPage().closeEditor(SwitchUserGraphicalEditor.this, save);
	}
	
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setEditPartFactory(new SwitchUserEditPartFactory());
		viewer.setRootEditPart(new ScalableFreeformRootEditPart());
		viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
		
	}
	@Override
	protected void initializeGraphicalViewer() {
		
		GraphicalViewer viewer = getGraphicalViewer();
		
		((SwitchUserGraphicalViewer) viewer).hookPaintListener();
		
		if(this.diagram != null){
			viewer.setContents(this.diagram);
			((SwitchUserGraphicalViewer) viewer).applyLayout();
		}
		
		
	}
	
	protected void createGraphicalViewer(Composite parent) {
		
		GraphicalViewer viewer = new SwitchUserGraphicalViewer();
		viewer.createControl(parent);
		setGraphicalViewer(viewer);
		configureGraphicalViewer();
		hookGraphicalViewer();
		initializeGraphicalViewer();
		
		
	}

	
	public SwitchUsersGroup getModel(){
		return this.diagram;
	}
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		if(input != null && input instanceof SwitchUserInput){
			this.diagram = ((SwitchUserInput)input).getSwitchUsersGroup();
		}
	}
	
	public void setName(String partName){
		this.setPartName(partName);
	}
	
	public boolean isDirty() {
		return false;
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {

	}

}
