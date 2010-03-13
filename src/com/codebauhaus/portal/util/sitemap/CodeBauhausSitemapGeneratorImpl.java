package com.codebauhaus.portal.util.sitemap;

import com.liferay.portal.model.LayoutSet;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;

/**
 * Created by CodeBauhaus.com
 * Date: Mar 11, 2010
 * Time: 6:48:55 PM
 */
public class CodeBauhausSitemapGeneratorImpl extends AbstractSitemapGenerator {


    public void getSitemap(Document doc , LayoutSet layoutSet, ThemeDisplay themeDisplay)
            throws PortalException, SystemException{

        
        Element root=setup(doc);

        //build the LR standard nav structure
        getDefaultSitemapComponent().buildDocument(root, layoutSet, themeDisplay);

        //add user urls
        getUsersSitemapComponent().buildDocument(root, layoutSet,themeDisplay);

        //add blog urls
        getBlogsSitemapComponent().buildDocument(root,layoutSet,themeDisplay);

    }


    private static AbstractSitemapComponent defaultSitemapComponent;
    private static AbstractSitemapComponent usersSitemapComponent;
    private static AbstractSitemapComponent blogsSitemapComponent;

    public static AbstractSitemapComponent getDefaultSitemapComponent() {
        return defaultSitemapComponent;
    }

    public  void setDefaultSitemapComponent(AbstractSitemapComponent defaultSitemapComponent) {
        this.defaultSitemapComponent = defaultSitemapComponent;
    }

    public static AbstractSitemapComponent getUsersSitemapComponent() {
        return usersSitemapComponent;
    }

    public  void setUsersSitemapComponent(AbstractSitemapComponent usersSitemapComponent) {
        this.usersSitemapComponent = usersSitemapComponent;
    }

    public static AbstractSitemapComponent getBlogsSitemapComponent() {
        return blogsSitemapComponent;
    }

    public  void setBlogsSitemapComponent(AbstractSitemapComponent blogsSitemapComponent) {
        this.blogsSitemapComponent = blogsSitemapComponent;
    }
}