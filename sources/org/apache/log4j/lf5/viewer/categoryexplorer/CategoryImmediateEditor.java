package org.apache.log4j.lf5.viewer.categoryexplorer;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.TreePath;

public class CategoryImmediateEditor extends DefaultTreeCellEditor {
    protected Icon editingIcon = null;
    private CategoryNodeRenderer renderer;

    public CategoryImmediateEditor(JTree tree, CategoryNodeRenderer renderer2, CategoryNodeEditor editor) {
        super(tree, renderer2, editor);
        this.renderer = renderer2;
        renderer2.setIcon((Icon) null);
        renderer2.setLeafIcon((Icon) null);
        renderer2.setOpenIcon((Icon) null);
        renderer2.setClosedIcon((Icon) null);
        this.editingIcon = null;
    }

    public boolean shouldSelectCell(EventObject e) {
        if (!(e instanceof MouseEvent)) {
            return false;
        }
        MouseEvent me = (MouseEvent) e;
        return ((CategoryNode) this.tree.getPathForLocation(me.getX(), me.getY()).getLastPathComponent()).isLeaf();
    }

    public boolean inCheckBoxHitRegion(MouseEvent e) {
        TreePath path = this.tree.getPathForLocation(e.getX(), e.getY());
        if (path == null) {
            return false;
        }
        CategoryNode categoryNode = (CategoryNode) path.getLastPathComponent();
        Rectangle bounds = this.tree.getRowBounds(this.lastRow);
        Dimension checkBoxOffset = this.renderer.getCheckBoxOffset();
        bounds.translate(this.offset + checkBoxOffset.width, checkBoxOffset.height);
        boolean rv = bounds.contains(e.getPoint());
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean canEditImmediately(EventObject e) {
        if (e instanceof MouseEvent) {
            return inCheckBoxHitRegion((MouseEvent) e);
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void determineOffset(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
        this.offset = 0;
    }
}
