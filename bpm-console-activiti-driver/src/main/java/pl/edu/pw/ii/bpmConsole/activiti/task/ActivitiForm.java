package pl.edu.pw.ii.bpmConsole.activiti.task;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.FormType;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.form.DateFormType;
import org.activiti.engine.impl.form.EnumFormType;
import org.activiti.engine.task.Task;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.AccessDeniedException;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.DateParsingException;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.NoSuchTaskException;
import pl.edu.pw.ii.bpmConsole.valueObjects.FieldInfo;
import pl.edu.pw.ii.bpmConsole.valueObjects.FormInfo;
import pl.edu.pw.ii.bpmConsole.valueObjects.Rights;
import pl.edu.pw.ii.bpmConsole.valueObjects.TaskInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ActivitiForm {

    private final ProcessEngine processEngine;

    public ActivitiForm(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    public FormInfo findFormForTask(String taskId, Rights rightsToTask) {
        TaskFormData taskForm = getFormData(taskId).orElseThrow(() -> new NoSuchTaskException(taskId));
        if (!rightsToTask.canRead())
            throw new AccessDeniedException(taskId);
        FormInfo formInfo = new FormInfo();
        formInfo.task = mapTask(taskForm.getTask());
        formInfo.description = taskForm.getTask().getDescription();
        formInfo.fields = mapFields(taskForm.getFormProperties());
        formInfo.rights = rightsToTask;
        return formInfo;
    }

    private Optional<TaskFormData> getFormData(String taskId) {
        return Optional.ofNullable(processEngine.getFormService().getTaskFormData(taskId));
    }

    private TaskInfo mapTask(Task task) {
        return new ActivitiTasks(processEngine.getTaskService(), processEngine.getRepositoryService()).mapTask(task);
    }

    private List<FieldInfo> mapFields(List<FormProperty> formProperties) {
        return formProperties
                .stream()
                .map(this::mapField)
                .collect(Collectors.toList());
    }

    private FieldInfo mapField(FormProperty formProperty) {
        FieldInfo fieldInfo = new FieldInfo();
        fieldInfo.id = formProperty.getId();
        fieldInfo.name = formProperty.getName();
        fieldInfo.type = formProperty.getType().getName();
        fieldInfo.required = formProperty.isRequired();
        fieldInfo.readOnly = !formProperty.isWritable();
        fieldInfo.value = mapValue(formProperty);
        fieldInfo.enumOptions = getEnumOptions(formProperty.getType());
        return fieldInfo;
    }

    private String mapValue(FormProperty formProperty) {
        if (formProperty.getValue() == null)
            return null;
        if (formProperty.getType() instanceof DateFormType) {
            return getValueDateInMilliseconds(formProperty);
        }
        return formProperty.getValue();
    }

    private String getValueDateInMilliseconds(FormProperty formProperty) {
        String datePattern = getDatePattern(formProperty);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
        try {
            return String.valueOf(simpleDateFormat.parse(formProperty.getValue()).getTime());
        } catch (ParseException e) {
            throw new DateParsingException(datePattern, formProperty.getValue());
        }
    }

    private String getDatePattern(FormProperty formProperty) {
        return (String) formProperty.getType().getInformation("datePattern");
    }

    private Map<String, String> getEnumOptions(FormType type) {
        if (type instanceof EnumFormType)
            return (Map<String, String>) type.getInformation("values");
        return null;
    }

}
