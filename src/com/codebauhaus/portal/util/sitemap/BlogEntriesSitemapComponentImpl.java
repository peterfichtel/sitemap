package com.codebauhaus.portal.util.sitemap;

import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.User;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutSet;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portlet.blogs.service.BlogsEntryLocalServiceUtil;
import com.liferay.portlet.blogs.model.BlogsEntry;

import java.util.List;

/**
 * Created by CodeBauhaus.com
 * Date: Mar 11, 2010
 * Time: 8:33:15 PM
 */
public class BlogEntriesSitemapComponentImpl extends AbstractSitemapComponent {


    public void buildDocument(Element element,
                                LayoutSet layoutSet, ThemeDisplay themeDisplay)
            throws PortalException, SystemException{

        Group suppliedGroup= GroupLocalServiceUtil.getGroup(layoutSet.getGroupId());

        addBlogEntries(element,suppliedGroup, themeDisplay);

    }

    //TODO: allow for dynamic blog url building classes to be injected    
    private  void addBlogEntries(Element element, Group suppliedGroup, ThemeDisplay themeDisplay)
            throws PortalException, SystemException {

        //get all users for this group
        List<User> users= UserLocalServiceUtil.getGroupUsers(suppliedGroup.getGroupId());


        for(User u:users){
            //get the owners group since their blogs should have been created within their home group
            Group usersOwnGroup= GroupLocalServiceUtil.getUserGroup(suppliedGroup.getCompanyId(), u.getUserId());

            int count= BlogsEntryLocalServiceUtil.getGroupEntriesCount(usersOwnGroup.getGroupId());
            List <BlogsEntry> entries=BlogsEntryLocalServiceUtil.getGroupEntries(usersOwnGroup.getGroupId(), 0, count);

            //only add users if they have blogs
            //TODO consider adding based upon other criteria, like a profile, etc
            if(entries!=null && entries.size()>0){

                //get the public layout for the group:
                long id= LayoutLocalServiceUtil.getDefaultPlid(usersOwnGroup.getGroupId(),false);
                Layout layout=LayoutLocalServiceUtil.getLayout(id);

                //index the user, as well
                StringBuffer baseUrl=new StringBuffer(themeDisplay.getPortalURL());
                //TODO figure out where web comes from
                baseUrl.append("/web");
                baseUrl.append(usersOwnGroup.getFriendlyURL());

                //for each blog, add the url to
                for(BlogsEntry blog:entries){
                    Element blogUrlElement = element.addElement("url");
                    StringBuffer blogUrl=new StringBuffer(baseUrl.toString());
                    blogUrl.append(layout.getFriendlyURL());
                    //I'm not sure where the seperator comes form
                    //I believe "blogs" is the name of the portlet on the page
                    //TODO figure out how to get rid of blogs
                    blogUrl.append("/-/blogs/");
                    blogUrl.append(blog.getUrlTitle());
                    blogUrlElement.addElement("loc").addText(encodeXML(blogUrl.toString()));
                    blogUrlElement.addElement("changefreq").addText("daily");


                }
            }

        }

    }

}
