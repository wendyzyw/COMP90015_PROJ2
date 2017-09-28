package sample;

import javafx.scene.control.TabPane;

public class RibbonBar {
    private TabPane tabPane;

    public RibbonBar(){
        tabPane = new TabPane();
        buildTabs();
    }

    public TabPane getRibbonBar(){
        return this.tabPane;
    }

    private void buildTabs(){
        HomeTab homeTab = new HomeTab();

        tabPane.getTabs().addAll(homeTab.getHomeTab());
    }
}
