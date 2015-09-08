/*
 * Copyright (c) 2002-2014, Mairie de Paris
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
package fr.paris.lutece.plugins.workflow.modules.formincrement.service;

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
import fr.paris.lutece.plugins.workflow.modules.formincrement.business.TaskFormIncrementConfig;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.resource.IResourceHistoryService;
import fr.paris.lutece.plugins.workflowcore.service.task.SimpleTask;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;

import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * TaskFormIncrement
 */
public class TaskFormIncrement extends SimpleTask
{
    private static final String FIELD_INFORMATION_COMPLEMENTARY = "module.workflow.formincrement.task_form_increment_config.label_task_information";
    private static final String MESSAGE_COMPLEMENTARY_INFORMATION_BEGIN = "module.workflow.formincrement.message.information_complementary.not_empty.begin";
    private static final String MESSAGE_COMPLEMENTARY_INFORMATION_END = "module.workflow.formincrement.message.information_complementary.not_empty.end";
    private static final String MESSAGE_METHOD_NOT_FOUND = "module.workflow.formincrement.message.information_complementary.method_not_found";
    private static final String FORM_GETTER_INFORMATION_COMPLEMENTARY = "getInfoComplementary";
    private static final String FORM_SETTER_INFORMATION_COMPLEMENTARY = "setInfoComplementary";
    private static final String BEAN_TASK_CONFIG_SERVICE = "workflow-formincrement.taskFormIncrementConfigService";

    // SERVICES
    @Inject
    @Named( BEAN_TASK_CONFIG_SERVICE )
    private ITaskConfigService _taskFormIncrementConfigService;
    @Inject
    private IResourceHistoryService _resourceHistoryService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void processTask( int nIdResourceHistory, HttpServletRequest request, Locale locale )
    {
        Plugin pluginForm = PluginService.getPlugin( FormPlugin.PLUGIN_NAME );
        Plugin pluginDirectory = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );
        Plugin pluginExportDirectory = PluginService.getPlugin( ExportdirectoryPlugin.PLUGIN_NAME );
        TaskFormIncrementConfig config = _taskFormIncrementConfigService.findByPrimaryKey( this.getId(  ) );
        ResourceHistory resourceHistory = _resourceHistoryService.findByPrimaryKey( nIdResourceHistory );

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
                        if ( ( counter == null ) || counter.equals( "" ) )
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
                        throw new RuntimeException( I18nService.getLocalizedString( 
                                MESSAGE_COMPLEMENTARY_INFORMATION_BEGIN, locale ) +
                            config.getIdInformationComplementary(  ) +
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void doRemoveConfig(  )
    {
        _taskFormIncrementConfigService.remove( this.getId(  ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle( Locale locale )
    {
        TaskFormIncrementConfig config = _taskFormIncrementConfigService.findByPrimaryKey( this.getId(  ) );

        if ( config != null )
        {
            return I18nService.getLocalizedString( FIELD_INFORMATION_COMPLEMENTARY, locale ) +
            config.getIdInformationComplementary(  );
        }

        return StringUtils.EMPTY;
    }
}
