package com.liferay.covoit;

import com.liferay.portal.kernel.events.SimpleAction;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portlet.expando.DuplicateColumnNameException;
import com.liferay.portlet.expando.DuplicateTableNameException;
import com.liferay.portlet.expando.model.ExpandoColumn;
import com.liferay.portlet.expando.model.ExpandoColumnConstants;
import com.liferay.portlet.expando.model.ExpandoTable;
import com.liferay.portlet.expando.service.ExpandoColumnLocalServiceUtil;
import com.liferay.portlet.expando.service.ExpandoTableLocalServiceUtil;

public class ExpandoStartupAction extends SimpleAction {
//comment test
	/**
	 * Creates custom fields
	 */
	public void run(String[] ids) {
		try {

	// Get a reference to the ExpandoTable (User class)

			ExpandoTable table = null;
			long companyId = Long.parseLong(ids[0]);

			try {
			 	table = ExpandoTableLocalServiceUtil.addDefaultTable(companyId, User.class.getName());
			}
			catch(DuplicateTableNameException dtne) {
			 	table = ExpandoTableLocalServiceUtil.getDefaultTable(
				 	companyId, User.class.getName());
			}

			long tableId = table.getTableId();
			
	// Add or get the ExpandoColumn ("team-field")			
			ExpandoColumn teamColumn = null;			
			try {
				teamColumn = ExpandoColumnLocalServiceUtil.addColumn(
					tableId, "team-field", ExpandoColumnConstants.STRING);

				// Add Properties
				UnicodeProperties properties = new UnicodeProperties();
				properties.setProperty(ExpandoColumnConstants.INDEX_TYPE, Boolean.TRUE.toString());
				properties.setProperty("display-type", "selection-list");
				teamColumn.setTypeSettingsProperties(properties);

				// Set Permissions
				setExpandoPermissions(teamColumn.getCompanyId(), teamColumn);
				
				ExpandoColumnLocalServiceUtil.updateExpandoColumn(teamColumn);			

			}
			catch(DuplicateColumnNameException dcne) {
				// Get the ExpandoColumn ("team-field")
				teamColumn = ExpandoColumnLocalServiceUtil.getColumn(tableId, "team-field");
				
			}
			
	// Add or get the ExpandoColumn ("city-field")	
			ExpandoColumn cityColumn = null;
			try {
				cityColumn = ExpandoColumnLocalServiceUtil.addColumn(
					tableId, "city-field", ExpandoColumnConstants.STRING);

				// Add Properties
				UnicodeProperties properties = new UnicodeProperties();
				properties.setProperty(ExpandoColumnConstants.INDEX_TYPE, Boolean.TRUE.toString());
				properties.setProperty("display-type", "selection-list");
				cityColumn.setTypeSettingsProperties(properties);			
				
				String defaultCities = "Bordeaux,Mérignac,Le Haillan,Bruges,St Médard en Jalles,St Jean d'Illac";
				cityColumn.setDefaultData(defaultCities);
				
				int type = 16;
				cityColumn.setType(type);
				
				// Set Permissions
				setExpandoPermissions(cityColumn.getCompanyId(), cityColumn);
				
				ExpandoColumnLocalServiceUtil.updateExpandoColumn(cityColumn);
				
			}
			catch(DuplicateColumnNameException dcne) {
				// Get the ExpandoColumn ("team-field")
				cityColumn = ExpandoColumnLocalServiceUtil.getColumn(tableId, "city-field");
				
			}
						
	// Add or get the ExpandoColumn ("phonenumber-field")	
			ExpandoColumn phoneNumberColumn = null;
			try {
				phoneNumberColumn = ExpandoColumnLocalServiceUtil.addColumn(
					tableId, "phonenumber-field", ExpandoColumnConstants.STRING);

				// Add Properties
				UnicodeProperties properties = new UnicodeProperties();
				properties.setProperty(ExpandoColumnConstants.INDEX_TYPE, Boolean.TRUE.toString());
				properties.setProperty("display-type", "selection-list");
				phoneNumberColumn.setTypeSettingsProperties(properties);

				ExpandoColumnLocalServiceUtil.updateExpandoColumn(phoneNumberColumn);
				
				// Set Permissions
				setExpandoPermissions(teamColumn.getCompanyId(), phoneNumberColumn);	
				
			}
			catch(DuplicateColumnNameException dcne) {
				// Get the ExpandoColumn ("team-field")
				phoneNumberColumn = ExpandoColumnLocalServiceUtil.getColumn(
						tableId, "phonenumber-field");
				
			}
			
	// Add or get the ExpandoColumn ("role-field")	
			ExpandoColumn roleColumn = null;
			try {
				roleColumn = ExpandoColumnLocalServiceUtil.addColumn(
					tableId, "role-field", ExpandoColumnConstants.STRING);

				// Add Properties
				UnicodeProperties properties = new UnicodeProperties();
				properties.setProperty(ExpandoColumnConstants.INDEX_TYPE, Boolean.TRUE.toString());
				properties.setProperty("display-type", "radio");
				roleColumn.setTypeSettingsProperties(properties);			
				
				String defaultRoles = "Conducteur,Passager,Les deux";
				roleColumn.setDefaultData(defaultRoles);
				
				int type = 16;
				roleColumn.setType(type);
				
				// Set Permissions
				setExpandoPermissions(roleColumn.getCompanyId(), roleColumn);
				
				ExpandoColumnLocalServiceUtil.updateExpandoColumn(roleColumn);
				
			}
			catch(DuplicateColumnNameException dcne) {
				// Get the ExpandoColumn ("role-field")
				roleColumn = ExpandoColumnLocalServiceUtil.getColumn(tableId, "role-field");
				
			}

		}
		catch(Exception e) {
			_log.error(e);
		}
	}
	
	/**
	 * Sets permissions for newly created fields
	 * 
	 * @param companyId
	 * @param column
	 * @throws SystemException
	 */
	private static void setExpandoPermissions(long companyId, ExpandoColumn column) throws SystemException {
      try {

          Role guestUserRole = RoleLocalServiceUtil.getRole(companyId, RoleConstants.GUEST);

          if (guestUserRole != null) {
                // define actions 
                String[] actionIds = new String[] { ActionKeys.VIEW, ActionKeys.UPDATE };
                // set the permission
                ResourcePermissionLocalServiceUtil.setResourcePermissions(companyId, 
                  ExpandoColumn.class.getName(), ResourceConstants.SCOPE_INDIVIDUAL, 
                  String.valueOf(column.getColumnId()), guestUserRole.getRoleId(), actionIds);
            }
      } catch (PortalException pe) {
    	  
      }
      
	}
	
	private static Log _log = LogFactoryUtil.getLog(ExpandoStartupAction.class);
	
}
