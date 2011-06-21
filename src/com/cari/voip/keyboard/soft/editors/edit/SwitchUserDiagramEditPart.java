package com.cari.voip.keyboard.soft.editors.edit;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;

import com.cari.voip.keyboard.soft.editors.edit.layout.SwitchUserLayout;
import com.cari.voip.keyboard.soft.editors.edit.policy.SwitchUserDiagramXYLayoutEditPolicy;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersGroup;

public class SwitchUserDiagramEditPart extends AbstractGraphicalEditPart
		implements PropertyChangeListener {

	@Override
	protected IFigure createFigure() {
		Figure f = new FreeformLayer();
		f.setBorder(new MarginBorder(3));
		f.setLayoutManager(new FreeformLayout());
		//f.setLayoutManager(new SwitchUserLayout());
		
		return f;
	}

	@Override
	protected void createEditPolicies() {
		//installEditPolicy(EditPolicy.CONTAINER_ROLE, new RootComponentEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, 
				new SwitchUserDiagramXYLayoutEditPolicy());
	}

	private SwitchUsersGroup getCastedModel() {
		return (SwitchUsersGroup) getModel();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	protected List getModelChildren() {
		return getCastedModel().getAllSwitchUsersList(); // return a list of shapes
	}
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub

	}

}
