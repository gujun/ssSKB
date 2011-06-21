package com.cari.voip.keyboard.soft.editors.edit;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUser;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersGroup;

public class SwitchUserEditPartFactory implements EditPartFactory {

	@Override
	public EditPart createEditPart(EditPart arg0, Object arg1) {
		// get EditPart for model element
		EditPart part = getPartForElement(arg1);
		// store model element in EditPart
		part.setModel(arg1);
		return part;
	}

	private EditPart getPartForElement(Object modelElement) {
		if(modelElement instanceof SwitchUser){
			return new SwitchUserEditPart();
		}
		if(modelElement instanceof SwitchUsersGroup){
			return new SwitchUserDiagramEditPart();
		}
		return null;
	}

}
