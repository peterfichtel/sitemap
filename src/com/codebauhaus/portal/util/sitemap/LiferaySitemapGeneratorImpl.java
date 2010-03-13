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
 * Time: 6:47:26 PM
 */
public class LiferaySitemapGeneratorImpl extends AbstractSitemapGenerator {

    public void getSitemap(Document doc, LayoutSet layoutSet, ThemeDisplay themeDisplay)
            throws PortalException, SystemException{


        Element root = setup(doc);

        getDefaultSitemapComponent().buildDocument(root, layoutSet, themeDisplay);

    }

    private static AbstractSitemapComponent defaultSitemapComponent;

    public static AbstractSitemapComponent getDefaultSitemapComponent() {
        return defaultSitemapComponent;
    }

    public  void setDefaultSitemapComponent(AbstractSitemapComponent defaultSitemapComponent) {
        this.defaultSitemapComponent = defaultSitemapComponent;
    }
}
