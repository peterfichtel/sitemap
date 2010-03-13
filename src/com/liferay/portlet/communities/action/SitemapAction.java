package com.liferay.portlet.communities.action;

import com.liferay.portal.NoSuchLayoutSetException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.LayoutSet;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.LayoutSetLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.WebKeys;
import com.liferay.util.servlet.ServletResponseUtil;
import com.codebauhaus.portal.util.sitemap.AbstractSitemapGenerator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


/**

 */

/**
 * <a href="SitemapAction.java.html"><b><i>View Source</i></b></a>
 *
 * @author Jorge Ferrer
 *
 * Modified by CodeBauhaus.com to use injected classes that define how the map is drawn
 * Date: Mar 11, 2010
 * Time: 6:41:46 PM
 *
 */
public class SitemapAction extends Action {

    public ActionForward execute(
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response)
		throws Exception {

		try {
			ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
				WebKeys.THEME_DISPLAY);

			long groupId = ParamUtil.getLong(request, "groupId");
			boolean privateLayout = ParamUtil.getBoolean(
				request, "privateLayout");

			LayoutSet layoutSet = null;

			if (groupId > 0) {
				Group group = GroupLocalServiceUtil.getGroup(groupId);

				if (group.isStagingGroup()) {
					groupId = group.getLiveGroupId();
				}

				layoutSet = LayoutSetLocalServiceUtil.getLayoutSet(
					groupId, privateLayout);
			}
			else {
				String host = PortalUtil.getHost(request);

				layoutSet = LayoutSetLocalServiceUtil.getLayoutSet(host);
			}

            Document sitemapXml = SAXReaderUtil.createDocument();
            //now use the class defined in the spring config file to draw it.
            //each class can have any number of things to add to the sitemap
			getSitemapGenerator().getSitemap(sitemapXml,layoutSet,themeDisplay);

            String sitemap=sitemapXml.asXML();

			ServletResponseUtil.sendFile(
				response, null, sitemap.getBytes(StringPool.UTF8),
				ContentTypes.TEXT_XML_UTF8);
		}
		catch (NoSuchLayoutSetException nslse) {
			PortalUtil.sendError(
				HttpServletResponse.SC_NOT_FOUND, nslse, request, response);
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(e, e);
			}

			PortalUtil.sendError(
				HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e, request,
				response);
		}

		return null;
	}

    private static AbstractSitemapGenerator abstractSitemapGenerator;

    public void setSitemapGenerator(AbstractSitemapGenerator abstractSitemapGenerator) {
        this.abstractSitemapGenerator = abstractSitemapGenerator;
    }

    public static AbstractSitemapGenerator getSitemapGenerator() {
        return abstractSitemapGenerator;
    }

    private static Log _log = LogFactoryUtil.getLog(SitemapAction.class);

}