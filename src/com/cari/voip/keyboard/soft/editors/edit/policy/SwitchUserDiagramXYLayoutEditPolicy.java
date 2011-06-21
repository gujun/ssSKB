package com.cari.voip.keyboard.soft.editors.edit.policy;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import com.cari.voip.keyboard.soft.model.switchUsers.commands.SwitchUserGraphNodeSetConstraintCommand;

public class SwitchUserDiagramXYLayoutEditPolicy extends XYLayoutEditPolicy {

    protected Command createAddCommand(EditPart child, Object constraint) {
        // not used in this example
        return null;
    }
    
    protected Command createChangeConstraintCommand(
            ChangeBoundsRequest request, EditPart child, Object constraint) {
    	if(request.getType() == RequestConstants.REQ_MOVE_CHILDREN){
    		return new SwitchUserGraphNodeSetConstraintCommand(
    				child,request,(Rectangle)constraint);
    	}
    	
    	return super.createChangeConstraintCommand(request, child, constraint);
    }
	@Override
	protected Command createChangeConstraintCommand(EditPart child,
			Object constraint) {
		// TODO Auto-generated method stub
		return new Command(){
    		
    	};
	}

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	public Rectangle getCurrentConstraintFor(GraphicalEditPart child){
		IFigure fig = child.getFigure();
		Rectangle rectangle = (Rectangle) fig.getParent().getLayoutManager().getConstraint(fig);
		if (rectangle == null)
		{
			rectangle = fig.getBounds();
		}
		return rectangle;
	}
}
