package com.codebauhaus.portal.util.sitemap;

import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.util.*;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutSet;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.util.PortalUtil;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by CodeBauhaus.com
 * Date: Mar 11, 2010
 * Time: 8:15:34 PM
 */
public class DefaultSitemapComponentImpl extends AbstractSitemapComponent {

    public void buildDocument(Element element,
                              LayoutSet layoutSet, ThemeDisplay themeDisplay)
            throws PortalException, SystemException{


        List<Layout> layouts = LayoutLocalServiceUtil.getLayouts(layoutSet.getGroupId(), layoutSet.isPrivateLayout());

        _visitLayouts(element, layouts, themeDisplay);

    }

    //this was taken directly from sitemaputil in portal source.
    //removed recursion b/c it was causing duplicate entries for children
    //not sure why recursion was used
    private void _visitLayouts(
            Element element, List<Layout> layouts, ThemeDisplay themeDisplay)
            throws PortalException, SystemException {

        Map urls=new HashMap();

        for (Layout layout : layouts) {
            UnicodeProperties props = layout.getTypeSettingsProperties();

            if (PortalUtil.isLayoutSitemapable(layout) && !layout.isHidden() &&
                    GetterUtil.getBoolean(
                            props.getProperty("sitemap-include"), true)) {



                String layoutURL = PortalUtil.getLayoutURL(
                        layout, themeDisplay);

                if (!HttpUtil.hasDomain(layoutURL)) {
                    layoutURL = themeDisplay.getPortalURL() + layoutURL;
                }

                //don't record duplicate urls
                if(!urls.containsKey(layoutURL)){
                    urls.put(layoutURL,layoutURL);
                    Element url = element.addElement("url");


                    url.addElement("loc").addText(encodeXML(layoutURL));

                    String changefreq = props.getProperty("sitemap-changefreq");

                    if (Validator.isNotNull(changefreq)) {
                        url.addElement("changefreq").addText(changefreq);
                    }

                    String priority = props.getProperty("sitemap-priority");

                    if (Validator.isNotNull(priority)) {
                        url.addElement("priority").addText(priority);
                    }
                }

                //I don't understand what this does, as it creates duplicate information with children items
                //List<Layout> children = layout.getChildren();

                //_visitLayouts(element, children, themeDisplay);
            }
        }
    }

}
