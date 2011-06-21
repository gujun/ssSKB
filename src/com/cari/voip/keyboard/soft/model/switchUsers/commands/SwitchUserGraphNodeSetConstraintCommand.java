package com.cari.voip.keyboard.soft.model.switchUsers.commands;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;

import com.cari.voip.keyboard.soft.editors.edit.SwitchUserEditPart;

public class SwitchUserGraphNodeSetConstraintCommand extends Command {

	private EditPart part;
	private Rectangle bounds;
	private ChangeBoundsRequest request;
	
	public SwitchUserGraphNodeSetConstraintCommand(EditPart part,ChangeBoundsRequest request,
            Rectangle rectangle){
		this.part = part;
		this.bounds = rectangle;
		this.request = request;
	}
	public void execute() {
		if(this.part instanceof SwitchUserEditPart){
			((SwitchUserEditPart)part).getFigure().setBounds(bounds);
		}
		
	}
}
