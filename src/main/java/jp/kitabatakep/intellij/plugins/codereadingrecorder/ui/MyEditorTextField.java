package jp.kitabatakep.intellij.plugins.codereadingrecorder.ui;

import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.ui.EditorTextField;

public class MyEditorTextField extends EditorTextField
{
    public MyEditorTextField(Project project, FileType fileType)
    {
        super(project, fileType);
    }

    @Override
    protected EditorEx createEditor()
    {
        EditorEx editor = super.createEditor();
        editor.setVerticalScrollbarVisible(true);
        editor.setHorizontalScrollbarVisible(true);
        return editor;
    }
}
