package com.codebauhaus.portal.util.sitemap;

import com.liferay.portal.model.LayoutSet;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.util.StringPool;

/**
 * Created by CodeBauhaus.com
 * Date: Mar 11, 2010
 * Time: 6:46:21 PM
 */
public abstract class AbstractSitemapGenerator {

    public abstract void getSitemap(Document doc ,LayoutSet layoutSet, ThemeDisplay themeDisplay)
            throws PortalException, SystemException;

    protected Element setup(Document doc){

        doc.setXMLEncoding(StringPool.UTF8);

        Element root = doc.addElement(
                "urlset", "http://www.google.com/schemas/sitemap/0.84");

        return root;
    }
    
}
