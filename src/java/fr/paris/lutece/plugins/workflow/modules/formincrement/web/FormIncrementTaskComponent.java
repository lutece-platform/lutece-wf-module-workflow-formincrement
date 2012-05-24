/*
 * Copyright (c) 2002-2012, Mairie de Paris
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
package fr.paris.lutece.plugins.workflow.modules.formincrement.web;

import fr.paris.lutece.plugins.workflow.modules.formincrement.business.TaskFormIncrementConfig;
import fr.paris.lutece.plugins.workflow.modules.formincrement.service.FormIncrementPlugin;
import fr.paris.lutece.plugins.workflow.modules.formincrement.service.ITaskFormIncrementConfigService;
import fr.paris.lutece.plugins.workflow.utils.WorkflowUtils;
import fr.paris.lutece.plugins.workflow.web.task.NoFormTaskComponent;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * FormIncrementTaskComponent
 *
 */
public class FormIncrementTaskComponent extends NoFormTaskComponent
{
    // TEMPLATES
    private static final String TEMPLATE_TASK_FORM_INCREMENT_CONFIG = "admin/plugins/workflow/modules/formincrement/task_form_increment_config.html";

    // MARKS
    private static final String MARK_CONFIG = "config";
    private static final String MARK_INFORMATION_LIST = "information_list";

    // PARAMETERS
    private static final String PARAMETER_INFORMATION_COMPLEMENTARY = "id_information";

    // FIELDS
    private static final String FIELD_INFORMATION_COMPLEMENTARY = "module.workflow.formincrement.task_form_increment_config.label_task_information";

    // MESSAGES
    private static final String MESSAGE_MANDATORY_FIELD = "module.workflow.formincrement.message.mandatory.field";

    // SERVICES
    @Inject
    private ITaskFormIncrementConfigService _taskFormIncrementConfigService;

    /**
     * {@inheritDoc}
     */
    @Override
    public String doSaveConfig( HttpServletRequest request, Locale locale, ITask task )
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

        Plugin plugin = PluginService.getPlugin( FormIncrementPlugin.PLUGIN_NAME );
        TaskFormIncrementConfig config = _taskFormIncrementConfigService.findByPrimaryKey( task.getId(  ), plugin );
        Boolean bCreate = false;

        if ( config == null )
        {
            config = new TaskFormIncrementConfig(  );
            config.setIdTask( task.getId(  ) );
            bCreate = true;
        }

        config.setIdInformationComplementary( nIdInformationComplementary );

        if ( bCreate )
        {
            _taskFormIncrementConfigService.create( config, plugin );
        }
        else
        {
            _taskFormIncrementConfigService.update( config, plugin );
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayConfigForm( HttpServletRequest request, Locale locale, ITask task )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );
        Plugin plugin = PluginService.getPlugin( FormIncrementPlugin.PLUGIN_NAME );
        TaskFormIncrementConfig config = _taskFormIncrementConfigService.findByPrimaryKey( task.getId(  ), plugin );

        ReferenceList informationlist = new ReferenceList(  );
        informationlist.addItem( -1, StringUtils.EMPTY );

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

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayTaskInformation( int nIdHistory, HttpServletRequest request, Locale locale, ITask task )
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTaskInformationXml( int nIdHistory, HttpServletRequest request, Locale locale, ITask task )
    {
        return null;
    }
}
