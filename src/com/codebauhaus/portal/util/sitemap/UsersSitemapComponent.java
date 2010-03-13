package com.codebauhaus.portal.util.sitemap;

import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.model.LayoutSet;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portlet.blogs.service.BlogsEntryLocalServiceUtil;
import com.liferay.portlet.blogs.model.BlogsEntry;

import java.util.List;

/**
 * Created by CodeBauhaus.com
 * Date: Mar 11, 2010
 * Time: 8:32:52 PM
 */
public class UsersSitemapComponent extends AbstractSitemapComponent {
    public void buildDocument(Element element,
                                LayoutSet layoutSet, ThemeDisplay themeDisplay)
            throws PortalException, SystemException {


        Group suppliedGroup= GroupLocalServiceUtil.getGroup(layoutSet.getGroupId());

        addUsers(element,suppliedGroup, themeDisplay);        

    }

    //TODO: allow for dynamic user url building classes to be injected
    private  void addUsers(Element element, Group suppliedGroup, ThemeDisplay themeDisplay)
            throws PortalException, SystemException {

        //get all users
        List<User> users= UserLocalServiceUtil.getGroupUsers(suppliedGroup.getGroupId());

        //we're only displaying users with blogs for now
        for(User u:users){
            Group usersOwnGroup= GroupLocalServiceUtil.getUserGroup(suppliedGroup.getCompanyId(), u.getUserId());

            int count= BlogsEntryLocalServiceUtil.getGroupEntriesCount(usersOwnGroup.getGroupId());
            List <BlogsEntry> entries=BlogsEntryLocalServiceUtil.getGroupEntries(usersOwnGroup.getGroupId(), 0, count);

            //only add if the user has blogs
            if(entries!=null && entries.size()>0){

                //index the user, as well
                StringBuffer baseUrl=new StringBuffer(themeDisplay.getPortalURL());
                
                //TODO figure out where web comes from
                baseUrl.append("/web");
                baseUrl.append(usersOwnGroup.getFriendlyURL());

                Element userUrlElement = element.addElement("url");

                userUrlElement.addElement("loc").addText(encodeXML(baseUrl.toString()));
                userUrlElement.addElement("changefreq").addText("daily");
            }
        }

    }
}
