package edu.jhu.wwalke24;

import javafx.collections.ObservableList;

public interface Rowable {
	String[] getColumnNames();
	ObservableList<String> toRow();
	String getId();
}
