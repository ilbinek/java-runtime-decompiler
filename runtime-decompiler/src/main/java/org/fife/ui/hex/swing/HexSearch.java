package org.fife.ui.hex.swing;

import java.util.ArrayList;

public class HexSearch {

    private HexEditor hex;
    private SearchState searchState;

    public enum HexSearchOptions {
        HEX,
        INT,
        TEXT
    }

    public HexSearch(HexEditor hex) {
        this.hex = hex;
        this.searchState = new SearchState(0, 0, false);
    }

    public SearchState getSearchState() {
        return searchState;
    }

    private ArrayList<Byte> getByteArray(String str, HexSearchOptions type) {
        ArrayList<Byte> arr = new ArrayList<>();
        switch (type) {
            case TEXT:
                byte[] bytesText = str.getBytes();
                if (bytesText.length == 0) {
                    throw new StringIndexOutOfBoundsException();
                }
                for (byte b : bytesText) {
                    arr.add(b);
                }
                break;

            case INT:
                if (str.trim().equals("")) {
                    throw new StringIndexOutOfBoundsException();
                }
                String[] spliced = str.split(" ");
                for (String s : spliced) {
                    arr.add(Integer.valueOf(s).byteValue());
                }
                break;

            case HEX:
                if (str.trim().equals("")) {
                    throw new StringIndexOutOfBoundsException();
                }
                String[] splicedHex = str.split(" ");
                for (String s : splicedHex) {
                    int i = Integer.parseInt(s, 16);
                    arr.add(Integer.valueOf(i).byteValue());
                }
                break;

            default:
                throw new HexSearchParseException("Unknown parser argument");
        }
        return arr;
    }

    public boolean searchHexCode(String str, HexSearchOptions type) {
        searchState = new SearchState(0, 0, false);
        ArrayList<Byte> arr = getByteArray(str, type);
        findMatch(arr,0);
        if (searchState.isFound()) {
            hex.setSelectedRange(searchState.getStart(), searchState.getEnd() - 1);
            return true;
        }
        return false;
    }

    private boolean findMatch(ArrayList<Byte> arr, int start) {
        int byteCount = hex.getByteCount();
        for (int i = start; i < byteCount; i++) {
            if (arr.get(0) == hex.getByte(i)) {
                if (checkIfMatches(arr, i)) {
                    searchState = new SearchState(i, i + arr.size(), true);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkIfMatches(ArrayList<Byte> arr, int start) {
        for (int i = 0; i < arr.size(); i++) {
            if (!(arr.get(i) == hex.getByte(i + start))) {
                return false;
            }
        }
        return true;
    }

    public boolean next(String str, HexSearchOptions type) {
        if (!searchState.isFound()) {
            return false;
        }
        ArrayList<Byte> arr = getByteArray(str, type);
        searchState = new SearchState(searchState.getStart(), searchState.getEnd(), false);
        findMatch(arr, searchState.getStart() + 1);
        if (searchState.isFound()) {
            hex.setSelectedRange(searchState.getStart(), searchState.getEnd() - 1);
            return true;
        } else {
            searchState = new SearchState(searchState.getStart(), searchState.getEnd(), true);
            return false;
        }
    }

    public boolean previous(String str, HexSearchOptions type) {
        if (!searchState.isFound()) {
            return false;
        }
        ArrayList<Byte> arr = getByteArray(str, type);
        searchState = new SearchState(searchState.getStart(), searchState.getEnd(), false);
        for (int i = searchState.getStart() - 1; i > 0; i--) {
            if (arr.get(0) == hex.getByte(i)) {
                if (checkIfMatches(arr, i)) {
                    searchState = new SearchState(i, i + arr.size(), true);
                    break;
                }
            }
        }
        if (searchState.isFound()) {
            hex.setSelectedRange(searchState.getStart(), searchState.getEnd() - 1);
            return true;
        } else {
            searchState = new SearchState(searchState.getStart(), searchState.getEnd(), true);
            return false;
        }
    }
}
