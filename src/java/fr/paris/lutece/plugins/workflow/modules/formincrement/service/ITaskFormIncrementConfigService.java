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
package fr.paris.lutece.plugins.workflow.modules.formincrement.service;

import fr.paris.lutece.plugins.workflow.modules.formincrement.business.TaskFormIncrementConfig;
import fr.paris.lutece.portal.service.plugin.Plugin;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 *
 * ITaskFormIncrementConfigService
 *
 */
public interface ITaskFormIncrementConfigService
{
    /**
    * Insert new configuration
    * @param config object configuration
    * @param plugin the plugin
    */
    @Transactional( "workflow-formincrement.transactionManager" )
    void create( TaskFormIncrementConfig config, Plugin plugin );

    /**
     * Update a configuration
     * @param config object configuration
     * @param plugin the plugin
     */
    @Transactional( "workflow-formincrement.transactionManager" )
    void update( TaskFormIncrementConfig config, Plugin plugin );

    /**
     * Delete a configuration
     * @param nIdTask id task
     * @param plugin the plugin
     */
    @Transactional( "workflow-formincrement.transactionManager" )
    void remove( int nIdTask, Plugin plugin );

    // Finders

    /**
     * Delete a configuration
     * @param nIdTask id task
     * @param plugin the plugin
     * @return a configuration
     */
    TaskFormIncrementConfig findByPrimaryKey( int nIdTask, Plugin plugin );

    /**
     * Load All Directorty
     * @param plugin the plugin
     * @return a configuration
     */
    List<TaskFormIncrementConfig> getAll( Plugin plugin );
}
