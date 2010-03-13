package com.codebauhaus.portal.util.sitemap;

import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.util.StringUtil;

import com.liferay.portal.model.LayoutSet;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;

/**
 * Created by CodeBauhaus.com
 * Date: Mar 11, 2010
 * Time: 8:13:45 PM
 */
public abstract class AbstractSitemapComponent {

    public abstract void buildDocument(Element element,
                                LayoutSet layoutSet, ThemeDisplay themeDisplay)
                    throws PortalException, SystemException;



    protected String encodeXML(String input) {
        return StringUtil.replace(
                input,
                new String[] {"&", "<", ">", "'", "\""},
                new String[] {"&amp;", "&lt;", "&gt;", "&apos;", "&quot;"});
    }    
    
}
