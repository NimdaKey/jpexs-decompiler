/*
 *  Copyright (C) 2010-2015 JPEXS
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jpexs.decompiler.flash.gui.generictageditors;

import com.jpexs.decompiler.flash.gui.AppStrings;
import com.jpexs.decompiler.flash.gui.MainPanel;
import com.jpexs.helpers.ByteArrayRange;
import com.jpexs.helpers.Helper;
import com.jpexs.helpers.ReflectionTools;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Field;
import javax.swing.JButton;

/**
 *
 * @author JPEXS
 */
public class BinaryDataEditor extends JButton implements GenericTagEditor {

    private final MainPanel mainPanel;

    private final Object obj;

    private final Field field;

    private final int index;

    private final Class<?> type;

    private final String fieldName;

    private Object value;

    public BinaryDataEditor(MainPanel mainPanel, String fieldName, Object obj, Field field, int index, Class<?> type) {
        super();
        this.mainPanel = mainPanel;
        this.obj = obj;
        this.field = field;
        this.index = index;
        this.type = type;
        this.fieldName = fieldName;
        setText(AppStrings.translate("button.replace"));
        addActionListener(this::buttonActionPerformed);

        try {
            ByteArrayRange bar = (ByteArrayRange) ReflectionTools.getValue(obj, field, index);
            setToolTipText(bar.getLength() + " bytes");
            value = bar;
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            // ignore
        }
    }

    private void buttonActionPerformed(ActionEvent evt) {
        File selectedFile = mainPanel.showImportFileChooser("");
        if (selectedFile != null) {
            File selfile = Helper.fixDialogFile(selectedFile);
            byte[] data = Helper.readFile(selfile.getAbsolutePath());
            setToolTipText(data.length + " bytes");
            value = new ByteArrayRange(data);
        }
    }

    @Override
    public void save() {
        try {
            ReflectionTools.setValue(obj, field, index, value);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            // ignore
        }
    }

    @Override
    public void addChangeListener(final ChangeListener l) {
        final GenericTagEditor t = this;
        addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                l.change(t);
            }
        });
    }

    @Override
    public Object getChangedValue() {
        return value;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public String getReadOnlyValue() {
        return getChangedValue().toString();
    }

    @Override
    public BaselineResizeBehavior getBaselineResizeBehavior() {
        return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
    }

    @Override
    public int getBaseline(int width, int height) {
        return 0;
    }
}
