package pl.edu.pw.ii.bpmConsole.activiti.task;

import org.activiti.engine.form.AbstractFormType;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.form.*;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.DateParsingException;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.NoSuchTaskException;
import pl.edu.pw.ii.bpmConsole.valueObjects.FieldInfo;
import pl.edu.pw.ii.bpmConsole.valueObjects.FormInfo;
import pl.edu.pw.ii.bpmConsole.valueObjects.Rights;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static pl.edu.pw.ii.bpmConsole.test.AssertJThrowableAssert.assertThrown;

@RunWith(MockitoJUnitRunner.class)
public class FormSpec extends AbstractTaskSpec {

    public static final String TASK_DESCRIPTION = "description";

    @Test
    public void shouldThrowExceptionIfTaskDoesntExist() {
        //given
        when(formServiceMock.getTaskFormData(TASK_ID)).thenReturn(null);
        //when - then
        assertThrown(() -> new ActivitiForm(processEngineMock).findFormForTask(TASK_ID, Rights.CLAIM))
                .isInstanceOf(NoSuchTaskException.class);
    }

    @Test
    public void shouldReturnEmptyFieldsListForNoForm() {
        //given
        TaskFormDataImpl taskForm = new TaskFormDataImpl();
        taskForm.setTask(new TaskEntity());
        taskForm.setFormProperties(Collections.emptyList());
        when(formServiceMock.getTaskFormData(TASK_ID)).thenReturn(taskForm);
        whenLookingForProcessDefinitonNameReturn(PROCESS_DEFINITION_NAME);
        //when
        FormInfo formInfo = new ActivitiForm(processEngineMock).findFormForTask(TASK_ID, Rights.CLAIM);
        //then
        assertThat(formInfo.fields).isEmpty();
    }

    @Test
    public void shouldReturnFormInfoWithTaskDataAndFieldsList() {
        //given
        TaskFormData testFormData = testFormData(false);
        when(formServiceMock.getTaskFormData(TASK_ID)).thenReturn(testFormData);
        whenLookingForProcessDefinitonNameReturn(PROCESS_DEFINITION_NAME);
        //when
        FormInfo formInfo = new ActivitiForm(processEngineMock).findFormForTask(TASK_ID, Rights.CLAIM);
        //then
        assertThat(formInfo.description).isEqualTo(TASK_DESCRIPTION);
        assertThat(formInfo.fields).hasSameSizeAs(testFormData.getFormProperties());
        verifyFormData(formInfo.fields, testFormData.getFormProperties());
    }

    @Test
    public void shouldThrowDateParsingExceptionIfDateIsUnparsable() {
        //given
        TaskFormData testFormData = testFormData(true);
        when(formServiceMock.getTaskFormData(TASK_ID)).thenReturn(testFormData);
        whenLookingForProcessDefinitonNameReturn(PROCESS_DEFINITION_NAME);
        //when - then
        assertThrown(() -> new ActivitiForm(processEngineMock).findFormForTask(TASK_ID, Rights.CLAIM))
                .isInstanceOf(DateParsingException.class);
    }

    private TaskFormData testFormData(boolean includeUnparsableDate) {
        TaskFormDataImpl taskFormData = new TaskFormDataImpl();
        taskFormData.setTask(new TaskEntity());
        taskFormData.getTask().setDescription(TASK_DESCRIPTION);
        List<FormProperty> formProperties = new ArrayList<>();
        formProperties.add(createFormProperty("first", "First", new StringFormType(), "value", true, false));
        formProperties.add(createFormProperty("second", "Second", new EnumFormType(Collections.singletonMap("id", "value")), null, false, true));
        formProperties.add(createFormProperty("third", "Third", new DateFormType("yyyy-MM-dd"), "2014-03-05", false, true));
        if (includeUnparsableDate)
            formProperties.add(createFormProperty("fourth", "Fourth", new DateFormType("yyyy-MM-dd"), "2014-03-aa", false, true));
        taskFormData.setFormProperties(formProperties);
        return taskFormData;
    }

    private FormPropertyImpl createFormProperty(String id, String name, AbstractFormType type, String value, boolean isRequired, boolean isWritable) {
        FormPropertyHandler formPropertyHandler = new FormPropertyHandler();
        formPropertyHandler.setId(id);
        formPropertyHandler.setName(name);
        formPropertyHandler.setType(type);
        formPropertyHandler.setRequired(isRequired);
        formPropertyHandler.setWritable(isWritable);
        FormPropertyImpl formProperty = new FormPropertyImpl(formPropertyHandler);
        formProperty.setValue(value);
        return formProperty;
    }

    private void verifyFormData(List<FieldInfo> fields, List<FormProperty> formProperties) {
        for (int i = 0; i < fields.size(); i++) {
            FieldInfo field = fields.get(i);
            FormProperty formProperty = formProperties.get(i);
            verifySingleFormField(field, formProperty);
        }
    }

    private void verifySingleFormField(FieldInfo field, FormProperty formProperty) {
        assertThat(field.id).isEqualTo(formProperty.getId());
        assertThat(field.name).isEqualTo(formProperty.getName());
        assertThat(field.type).isEqualTo(formProperty.getType().getName());
        verifyFormFIeldValue(field, formProperty);
        assertThat(field.required).isEqualTo(formProperty.isRequired());
        assertThat(field.readOnly).isEqualTo(!formProperty.isWritable());
        if (formProperty.getType() instanceof EnumFormType)
            verifyEnumOptions(field.enumOptions, (Map<String, String>) formProperty.getType().getInformation("values"));
    }

    private void verifyFormFIeldValue(FieldInfo field, FormProperty formProperty) {
        if (formProperty.getType() instanceof DateFormType) {
            String datePattern = (String) formProperty.getType().getInformation("datePattern");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
            String miliseconds;
            try {
                miliseconds = String.valueOf(simpleDateFormat.parse(formProperty.getValue()).getTime());
            } catch (ParseException e) {
                throw new AssertionError("Date's not parsable");
            }
            assertThat(field.value).isEqualTo(miliseconds);
        } else
            assertThat(field.value).isEqualTo(formProperty.getValue());
    }

    private void verifyEnumOptions(Map<String, String> enumOptions, Map<String, String> activitiEnumOptions) {
        for (Map.Entry<String, String> activitiEnumOption : activitiEnumOptions.entrySet())
            assertThat(enumOptions.get(activitiEnumOption.getKey())).isEqualTo(activitiEnumOption.getValue());
    }

}
