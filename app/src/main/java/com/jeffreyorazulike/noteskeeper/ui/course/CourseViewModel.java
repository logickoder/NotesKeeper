package com.jeffreyorazulike.noteskeeper.ui.course;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.jeffreyorazulike.noteskeeper.db.dao.CourseInfo;
import com.jeffreyorazulike.noteskeeper.db.dao.ModuleInfo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CourseViewModel extends ViewModel {

    private CourseInfo mCourse;

    CourseInfo getCourse() {
        return mCourse;
    }

    void saveCourse(@NonNull final CourseInfo course) {
        final List<ModuleInfo> modules = new ArrayList<>(course.getModules().size());
        for (ModuleInfo module: course.getModules())
            modules.add(new ModuleInfo(
                    module.getModuleId(), module.getTitle(), module.isComplete()));

        mCourse = new CourseInfo(
                course.getCourseId(), course.getTitle(), modules);
    }
}