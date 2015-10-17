package com.ldfs.theme.theme.read;


import com.ldfs.theme.theme.bean.ViewLayout;
import com.ldfs.theme.theme.util.ResUtils;
import com.ldfs.theme.util.XmlParser;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cz on 15/10/14.
 */
@Config("theme/view_layout.xml")
public class ViewLayoutReader extends AssetReader<String, ArrayList<ViewLayout>> {

    @Override
    public XmlParser.ParserListener getParserListener(HashMap<String, ArrayList<ViewLayout>> configs) {
        return new XmlParser.ParserListener() {
            private ArrayList<ViewLayout> items;
            private String tabName;

            @Override
            public void startParser(XmlPullParser parser) {
                String name = parser.getName();
                if ("layout".equals(name)) {
                    items = new ArrayList<>();
                    XmlParser.runParser(parser, (String... ts) -> {
                        if ("name".equals(ts[0])) {
                            tabName = ts[1];
                        }
                    });
                } else if ("item".equals(name)) {
                    ViewLayout viewLayout = new ViewLayout();
                    XmlParser.runParser(parser, (String... ts) -> {
                        if ("id".equals(ts[0])) {
                            viewLayout.id = ResUtils.id(ts[1]);
                        } else if ("layout".equals(ts[0])) {
                            viewLayout.layout = ts[1];
                        }
                    });
                    items.add(viewLayout);
                }
            }

            @Override
            public void endParser(XmlPullParser parser) {
                if ("layout".equals(parser.getName())) {
                    configs.put(tabName, items);
                }
            }
        };
    }
}
