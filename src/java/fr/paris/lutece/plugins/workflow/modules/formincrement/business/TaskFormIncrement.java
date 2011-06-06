/*
 * Copyright (c) 2002-2011, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.workflow.modules.formincrement.business;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.directory.business.RecordHome;
import fr.paris.lutece.plugins.directory.service.DirectoryPlugin;
import fr.paris.lutece.plugins.form.business.Form;
import fr.paris.lutece.plugins.form.business.FormHome;
import fr.paris.lutece.plugins.form.modules.exportdirectory.business.FormConfiguration;
import fr.paris.lutece.plugins.form.modules.exportdirectory.business.FormConfigurationHome;
import fr.paris.lutece.plugins.form.modules.exportdirectory.service.ExportdirectoryPlugin;
import fr.paris.lutece.plugins.form.service.FormPlugin;
import fr.paris.lutece.plugins.form.utils.FormUtils;
import fr.paris.lutece.plugins.workflow.business.ResourceHistory;
import fr.paris.lutece.plugins.workflow.business.ResourceHistoryHome;
import fr.paris.lutece.plugins.workflow.business.task.Task;
import fr.paris.lutece.plugins.workflow.utils.WorkflowUtils;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;


/**
 *
 * TaskFormIncrement
 */
public class TaskFormIncrement extends Task
{
    //templates
    private static final String TEMPLATE_TASK_FORM_INCREMENT_CONFIG = "admin/plugins/workflow/modules/formincrement/task_form_increment_config.html";

    //Marks
    private static final String MARK_CONFIG = "config";
    private static final String MARK_INFORMATION_LIST = "information_list";

    //Parameters
    private static final String PARAMETER_INFORMATION_COMPLEMENTARY = "id_information";

    //Fields
    private static final String FIELD_INFORMATION_COMPLEMENTARY = "module.workflow.formincrement.task_form_increment_config.label_task_information";

    //Message
    private static final String MESSAGE_MANDATORY_FIELD = "module.workflow.formincrement.message.mandatory.field";
    private static final String MESSAGE_COMPLEMENTARY_INFORMATION_BEGIN = "module.workflow.formincrement.message.information_complementary.not_empty.begin";
    private static final String MESSAGE_COMPLEMENTARY_INFORMATION_END = "module.workflow.formincrement.message.information_complementary.not_empty.end";
    private static final String MESSAGE_METHOD_NOT_FOUND = "module.workflow.formincrement.message.information_complementary.method_not_found";

    //Parameters
    private static final String FORM_GETTER_INFORMATION_COMPLEMENTARY = "getInfoComplementary";
    private static final String FORM_SETTER_INFORMATION_COMPLEMENTARY = "setInfoComplementary";

    /*
     * (non-Javadoc)
     * @see fr.paris.lutece.plugins.workflow.business.task.ITask#init()
     */
    public void init(  )
    {
    }

    /* (non-Javadoc)
     * @see fr.paris.lutece.plugins.workflow.business.ITaskListener#doSaveConfig(fr.paris.lutece.plugins.workflow.business.Action, javax.servlet.http.HttpServletRequest)
     */
    public String doSaveConfig( HttpServletRequest request, Locale locale, Plugin plugin )
    {
        String strIdInformationComplementary = request.getParameter( PARAMETER_INFORMATION_COMPLEMENTARY );

        int nIdInformationComplementary = Integer.parseInt( strIdInformationComplementary );

        String strError = WorkflowUtils.EMPTY_STRING;

        if ( nIdInformationComplementary == -1 )
        {
            strError = FIELD_INFORMATION_COMPLEMENTARY;
        }

        if ( !strError.equals( WorkflowUtils.EMPTY_STRING ) )
        {
            Object[] tabRequiredFields = { I18nService.getLocalizedString( strError, locale ) };

            return AdminMessageService.getMessageUrl( request, MESSAGE_MANDATORY_FIELD, tabRequiredFields,
                AdminMessage.TYPE_STOP );
        }

        TaskFormIncrementConfig config = TaskFormIncrementConfigHome.findByPrimaryKey( this.getId(  ), plugin );
        Boolean bCreate = false;

        if ( config == null )
        {
            config = new TaskFormIncrementConfig(  );
            config.setIdTask( this.getId(  ) );
            bCreate = true;
        }

        config.setIdInformationComplementary( nIdInformationComplementary );

        if ( bCreate )
        {
            TaskFormIncrementConfigHome.create( config, plugin );
        }
        else
        {
            TaskFormIncrementConfigHome.update( config, plugin );
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * @see fr.paris.lutece.plugins.workflow.business.task.ITask#doValidateTask(int, java.lang.String, javax.servlet.http.HttpServletRequest, java.util.Locale, fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public String doValidateTask( int nIdResource, String strResourceType, HttpServletRequest request, Locale locale,
        Plugin plugin )
    {
        return null;
    }

    /* (non-Javadoc)
     * @see fr.paris.lutece.plugins.workflow.business.ITaskListener#getDisplayConfigForm(fr.paris.lutece.plugins.workflow.business.Action, javax.servlet.http.HttpServletRequest)
     */
    public String getDisplayConfigForm( HttpServletRequest request, Plugin plugin, Locale locale )
    {
        HashMap<String, Object> model = new HashMap<String, Object>(  );

        TaskFormIncrementConfig config = TaskFormIncrementConfigHome.findByPrimaryKey( this.getId(  ), plugin );

        ReferenceList informationlist = new ReferenceList(  );
        informationlist.addItem( -1, "" );

        for ( int i = 1; i < 6; i++ )
        {
            informationlist.addItem( i, String.valueOf( i ) );
        }

        if ( config == null )
        {
            config = new TaskFormIncrementConfig(  );
            config.setIdInformationComplementary( -1 );
        }

        model.put( MARK_CONFIG, config );
        model.put( MARK_INFORMATION_LIST, informationlist );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_FORM_INCREMENT_CONFIG, locale, model );

        return template.getHtml(  );
    }

    /*
     * (non-Javadoc)
     * @see fr.paris.lutece.plugins.workflow.business.task.ITask#getDisplayTaskForm(int, java.lang.String, javax.servlet.http.HttpServletRequest, fr.paris.lutece.portal.service.plugin.Plugin, java.util.Locale)
     */
    public String getDisplayTaskForm( int nIdResource, String strResourceType, HttpServletRequest request,
        Plugin plugin, Locale locale )
    {
        return null;
    }

    /* (non-Javadoc)
     * @see fr.paris.lutece.plugins.workflow.business.ITaskListener#getDisplayTaskInformation(int, java.lang.String, javax.servlet.http.HttpServletRequest, fr.paris.lutece.plugins.workflow.business.Action)
     */
    public String getDisplayTaskInformation( int nIdHistory, HttpServletRequest request, Plugin plugin, Locale locale )
    {
        return null;
    }

    /* (non-Javadoc)
     * @see fr.paris.lutece.plugins.workflow.business.ITaskListener#processTask(int, java.lang.String, javax.servlet.http.HttpServletRequest, fr.paris.lutece.plugins.workflow.business.Action)
     */
    public void processTask( int nIdResourceHistory, HttpServletRequest request, Plugin plugin, Locale locale )
    {
        Plugin pluginForm = PluginService.getPlugin( FormPlugin.PLUGIN_NAME );
        Plugin pluginDirectory = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );
        Plugin pluginExportDirectory = PluginService.getPlugin( ExportdirectoryPlugin.PLUGIN_NAME );
        TaskFormIncrementConfig config = TaskFormIncrementConfigHome.findByPrimaryKey( this.getId(  ), plugin );
        ResourceHistory resourceHistory = ResourceHistoryHome.findByPrimaryKey( nIdResourceHistory, plugin );

        if ( ( config != null ) && ( resourceHistory != null ) )
        {
            Record record = RecordHome.findByPrimaryKey( resourceHistory.getIdResource(  ), pluginDirectory );
            int idForm = -1;
            Collection<FormConfiguration> listExportDirectory = FormConfigurationHome.findAll( pluginExportDirectory );

            for ( FormConfiguration formConfiguration : listExportDirectory )
            {
                if ( formConfiguration.getIdDirectory(  ) == record.getDirectory(  ).getIdDirectory(  ) )
                {
                    idForm = formConfiguration.getIdForm(  );
                }
            }

            Form form = FormHome.findByPrimaryKey( idForm, pluginForm );

            if ( form != null )
            {
                try
                {
                    String counter = (String) form.getClass(  )
                                                  .getMethod( FORM_GETTER_INFORMATION_COMPLEMENTARY +
                            config.getIdInformationComplementary(  ), (Class[]) null ).invoke( form, (Object[]) null );

                    try
                    {
                        if ( ( counter == null ) || counter.equals( FormUtils.EMPTY_STRING ) )
                        {
                            counter = "1";
                        }
                        else
                        {
                            counter = String.valueOf( Integer.parseInt( counter ) + 1 );
                        }

                        form.getClass(  )
                            .getMethod( FORM_SETTER_INFORMATION_COMPLEMENTARY +
                            config.getIdInformationComplementary(  ), String.class ).invoke( form, counter );
                        FormHome.update( form, pluginForm );
                    }
                    catch ( Exception e )
                    {
                        throw new RuntimeException( I18nService.getLocalizedString( MESSAGE_COMPLEMENTARY_INFORMATION_BEGIN,
                                locale ) + config.getIdInformationComplementary(  ) +
                            I18nService.getLocalizedString( MESSAGE_COMPLEMENTARY_INFORMATION_END, locale ), e );
                    }
                }
                catch ( Exception e )
                {
                	throw new RuntimeException( I18nService.getLocalizedString( MESSAGE_METHOD_NOT_FOUND, locale ) );
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see fr.paris.lutece.plugins.workflow.business.task.ITask#doRemoveConfig()
     */
    public void doRemoveConfig( Plugin plugin )
    {
        TaskFormIncrementConfigHome.remove( this.getId(  ), plugin );
    }

    /*
     * (non-Javadoc)
     * @see fr.paris.lutece.plugins.workflow.business.task.ITask#isConfigRequire()
     */
    public boolean isConfigRequire(  )
    {
        // TODO Auto-generated method stub
        return true;
    }

    /*
     * (non-Javadoc)
     * @see fr.paris.lutece.plugins.workflow.business.task.ITask#isFormTaskRequire()
     */
    public boolean isFormTaskRequire(  )
    {
        return false;
    }

    /*
     * (non-Javadoc)
     * @see fr.paris.lutece.plugins.workflow.business.task.ITask#doRemoveTaskInformation(int, fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public void doRemoveTaskInformation( int nIdHistory, Plugin plugin )
    {
    }

    /*
     * (non-Javadoc)
     * @see fr.paris.lutece.plugins.workflow.business.task.ITask#getTaskInformationXml(int, javax.servlet.http.HttpServletRequest, fr.paris.lutece.portal.service.plugin.Plugin, java.util.Locale)
     */
    public String getTaskInformationXml( int nIdHistory, HttpServletRequest request, Plugin plugin, Locale locale )
    {
        return null;
    }

    /*
     * (non-Javadoc)
     * @see fr.paris.lutece.plugins.workflow.business.task.ITask#getTitle(fr.paris.lutece.portal.service.plugin.Plugin, java.util.Locale)
     */
    public String getTitle( Plugin plugin, Locale locale )
    {
        TaskFormIncrementConfig config = TaskFormIncrementConfigHome.findByPrimaryKey( this.getId(  ), plugin );

        if ( config != null )
        {
            return I18nService.getLocalizedString( FIELD_INFORMATION_COMPLEMENTARY, locale ) +
            config.getIdInformationComplementary(  );
        }

        return WorkflowUtils.EMPTY_STRING;
    }

    /*
     * (non-Javadoc)
     * @see fr.paris.lutece.plugins.workflow.business.task.ITask#getTaskFormEntries(fr.paris.lutece.portal.service.plugin.Plugin, java.util.Locale)
     */
    public ReferenceList getTaskFormEntries( Plugin plugin, Locale locale )
    {
        return null;
    }

    /*
     * (non-Javadoc)
     * @see fr.paris.lutece.plugins.workflow.business.task.ITask#isTaskForActionAutomatic()
     */
    public boolean isTaskForActionAutomatic(  )
    {
        return true;
    }
}
