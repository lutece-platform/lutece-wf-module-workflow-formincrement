/*
 * Copyright (c) 2002-2009, Mairie de Paris
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

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * class TaskFormIncrementConfigDAO
 *
 */
public class TaskFormIncrementConfigDAO implements ITaskFormIncrementConfigDAO
{
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = "SELECT id_task,id_information " +
        "FROM task_form_increment_cf  WHERE id_task=?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO task_form_increment_cf( " + "id_task,id_information)" +
        "VALUES (?,?)";
    private static final String SQL_QUERY_UPDATE = "UPDATE task_form_increment_cf " + "SET id_task=?,id_information=?" +
        " WHERE id_task=?";
    private static final String SQL_QUERY_DELETE = "DELETE FROM task_form_increment_cf WHERE id_task=? ";
    private static final String SQL_QUERY_FIND_ALL = "SELECT id_task,id_information " + "FROM task_form_increment_cf";

    /*
     * (non-Javadoc)
     * @see fr.paris.lutece.plugins.workflow.modules.notifydirectory.business.ITaskNotifyDirectoryConfigDAO#insert(fr.paris.lutece.plugins.workflow.modules.notifydirectory.business.TaskNotifyDirectoryConfig, fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public synchronized void insert( TaskFormIncrementConfig config, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        int nPos = 0;

        daoUtil.setInt( ++nPos, config.getIdTask(  ) );
        daoUtil.setInt( ++nPos, config.getIdInformationComplementary(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /*
     * (non-Javadoc)
     * @see fr.paris.lutece.plugins.workflow.modules.notifydirectory.business.ITaskNotifyDirectoryConfigDAO#store(fr.paris.lutece.plugins.workflow.modules.bourserecherche.business.TaskEvaluationExpertConfig, fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public void store( TaskFormIncrementConfig config, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        int nPos = 0;

        daoUtil.setInt( ++nPos, config.getIdTask(  ) );
        daoUtil.setInt( ++nPos, config.getIdInformationComplementary(  ) );

        daoUtil.setInt( ++nPos, config.getIdTask(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /*
     * (non-Javadoc)
     * @see fr.paris.lutece.plugins.workflow.modules.notifydirectory.business.ITaskNotifyDirectoryConfigDAO#load(int, fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public TaskFormIncrementConfig load( int nIdTask, Plugin plugin )
    {
        TaskFormIncrementConfig config = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin );

        daoUtil.setInt( 1, nIdTask );

        daoUtil.executeQuery(  );

        int nPos = 0;

        if ( daoUtil.next(  ) )
        {
            config = new TaskFormIncrementConfig(  );
            config.setIdTask( daoUtil.getInt( ++nPos ) );
            config.setIdInformationComplementary( daoUtil.getInt( ++nPos ) );
        }

        daoUtil.free(  );

        return config;
    }

    /*
     * (non-Javadoc)
     * @see fr.paris.lutece.plugins.workflow.modules.notifydirectory.business.ITaskNotifyDirectoryConfigDAO#delete(int, fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public void delete( int nIdState, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );

        daoUtil.setInt( 1, nIdState );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /*
     * (non-Javadoc)
     * @see fr.paris.lutece.plugins.workflow.modules.notifydirectory.business.ITaskNotifyDirectoryConfigDAO#load(int, fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public List<TaskFormIncrementConfig> loadAll( Plugin plugin )
    {
        List<TaskFormIncrementConfig> configList = new ArrayList<TaskFormIncrementConfig>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_ALL, plugin );

        daoUtil.executeQuery(  );

        int nPos = 0;

        if ( daoUtil.next(  ) )
        {
            TaskFormIncrementConfig config = new TaskFormIncrementConfig(  );
            config.setIdTask( daoUtil.getInt( ++nPos ) );

            configList.add( config );
        }

        daoUtil.free(  );

        return configList;
    }
}
