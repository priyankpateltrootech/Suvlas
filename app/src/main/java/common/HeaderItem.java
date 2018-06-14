package common;

import com.brandongogetap.stickyheaders.exposed.StickyHeader;

import bean.MenuOrderItem;

public final class HeaderItem extends MenuOrderItem implements StickyHeader {

    public HeaderItem(String title, String message) {
        super("","","", title, "", "", 0);
    }
}