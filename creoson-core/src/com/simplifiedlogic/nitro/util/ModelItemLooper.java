/*
 * MIT LICENSE
 * Copyright 2000-2021 Simplified Logic, Inc
 * Permission is hereby granted, free of charge, to any person obtaining a copy 
 * of this software and associated documentation files (the "Software"), to deal 
 * in the Software without restriction, including without limitation the rights 
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell 
 * copies of the Software, and to permit persons to whom the Software is 
 * furnished to do so, subject to the following conditions: The above copyright 
 * notice and this permission notice shall be included in all copies or 
 * substantial portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", 
 * WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED 
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE 
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE 
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.simplifiedlogic.nitro.util;

import java.util.List;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModelItem.ModelItemType;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModel;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallModelItem;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallModelItems;
import com.simplifiedlogic.nitro.jlink.impl.JlinkUtils;
import com.simplifiedlogic.nitro.jlink.impl.NitroUtils;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * This is an abstract class which loops over ModelItems on an object, 
 * filters the results by various filters, and calls an action method
 * for each ModelItem which matches the filters.
 *  
 * @author Adam Andrews
 *
 */
public abstract class ModelItemLooper {

    private ModelItemType searchType = null;
    private String namePattern = null;
    private String[] nameList = null;
    private boolean isNamePattern = false;

    /**
     * The name of the current item in the loop
     */
    protected String currentName = null;
    
    /**
     * Loop through ModelItems on a model
     * @param m The model which owns the ModelItems
     * @throws JLIException
     * @throws jxthrowable
     */
    public void loop(CallModel m) throws JLIException,jxthrowable {
        // special case -- exact feature name given
        if (namePattern!=null && !isNamePattern) {
            currentName = null;
            CallModelItem item = null;
            if (searchType==ModelItemType.ITEM_DIMENSION) {
                int id = JlinkUtils.isDimId(namePattern);
                if (id>=0) {
                    try {
                        item = m.getItemById(ModelItemType.ITEM_DIMENSION, id);
                    } catch (Exception e) {
                        // if an item does not exist with the ID, an exception is thrown
                        item = null;
                    }
                }
                else {
                    item = m.getItemByName(searchType, namePattern);
                    if (item==null)
                        doLoop(m);
                }
            }
            else {
                item = m.getItemByName(searchType, namePattern);
                if (item==null)
                    doLoop(m);
            }
            if (item!=null)
                loopAction(item);
            return;
        }
        
        if (nameList!=null) {
            boolean[] found = new boolean[nameList.length];
            int foundcnt = 0;
            CallModelItem item = null;
            int id;
            for (int i=0; i<nameList.length; i++) {
                found[i] = false;
                currentName = null;
                if (searchType==ModelItemType.ITEM_DIMENSION) {
                    id = JlinkUtils.isDimId(nameList[i]);
                    if (id>=0) {
                        found[i] = true;  // don't do loop search for d# params
                        foundcnt++;
                        try {
                            item = m.getItemById(ModelItemType.ITEM_DIMENSION, id);
                        } catch (Exception e) {
                            // if an item does not exist with the ID, an exception is thrown
                            item = null;
                        }
                    }
                    else {
                        item = m.getItemByName(searchType, nameList[i]);
                    }
                }
                else {
                    item = m.getItemByName(searchType, nameList[i]);
                }
                if (item!=null) {
                    found[i] = true;
                    boolean abort = loopAction(item);
                    foundcnt++;
                    if (abort)
                        break;
                }
            }
            if (foundcnt<nameList.length) {
            	CallModelItems itemList = m.listItems(searchType);
                if (itemList!=null) {
                    int len = itemList.getarraysize();
                    item = null;
                    for (int i=0; i<len; i++) {
                        item = itemList.get(i);
                        currentName = item.getName();

                        for (int k=0; k<nameList.length; k++) {
                            if (!found[k]) {
                                if (currentName.toLowerCase().equals(nameList[k].toLowerCase())) {
                                    loopAction(item);
                                    found[k] = true;
                                    foundcnt++;
                                    break;
                                }
                            }
                        }
                        if (foundcnt>=nameList.length) 
                            break;
                    }
                }
            }
            return;
        }

        doLoop(m);
    }

    /**
     * Perform a loop on the model's ModelItems
     * @param m The model which owns the ModelItmes
     * @throws JLIException
     * @throws jxthrowable
     */
    private void doLoop(CallModel m) throws JLIException,jxthrowable {
    	CallModelItems itemList = m.listItems(searchType);
        if (itemList!=null) {
            int len = itemList.getarraysize();
            CallModelItem item = null;
            for (int i=0; i<len; i++) {
                currentName = null;
                item = itemList.get(i);

                if (!checkName(item))
                    continue;

                boolean abort = loopAction(item);
                if (abort)
                    break;
            }
        }
    }
    
    /**
     * Abstract function which is called for each ModelItem which matches the filters.
     * This must be implemented by a class extending this looper class.
     * @param item The ModelItem which matches the filters
     * @return True to abort the loop, false to continue it
     * @throws JLIException
     * @throws jxthrowable
     */
    public abstract boolean loopAction(CallModelItem item) throws JLIException, jxthrowable;

    /**
     * See whether the item matches the name filter
     * @param item The item to check
     * @return Whether the item matches the name filter
     * @throws jxthrowable
     */
    public boolean checkName(CallModelItem item) throws jxthrowable {
        if (namePattern!=null) {
            currentName = item.getName();
            if (isNamePattern && !currentName.toLowerCase().matches(namePattern))
                return false;
            if (!isNamePattern && !currentName.equalsIgnoreCase(namePattern))
                return false;
        }
        return true;
    }
    
    /**
     * @return The item name filter
     */
    public String getNamePattern() {
        return namePattern;
    }

    /**
     * @param namePattern The item name filter
     */
    public void setNamePattern(String namePattern) {
        if (namePattern!=null && namePattern.indexOf(' ')!=-1) {
            this.namePattern = null;
            this.nameList = namePattern.split(" ");
            return;
        }
        isNamePattern = NitroUtils.isPattern(namePattern);
        if (isNamePattern)
            this.namePattern = NitroUtils.transformPattern(namePattern.toLowerCase());
        else
            this.namePattern = namePattern;
    }

    /**
     * @return The ModelItem type filter
     */
    public ModelItemType getSearchType() {
        return searchType;
    }

    /**
     * @param searchType The ModelItem type filter
     */
    public void setSearchType(ModelItemType searchType) {
        this.searchType = searchType;
    }

    /**
     * @return The item name filter list 
     */
    public String[] getNameList() {
        return nameList;
    }

    /**
     * @param nameList The item name filter list
     */
    public void setNameList(List<String> nameList) {
        this.namePattern = null;
        this.isNamePattern = false;
        if (nameList!=null) {
            int len = nameList.size();
            this.nameList = new String[len];
            String name;
            for (int i=0; i<len; i++) {
                name = nameList.get(i);
                if (NitroUtils.isPattern(name)) {
                    this.isNamePattern=true;
                    this.nameList[i] = NitroUtils.transformPattern(name.toLowerCase());
                }
                else
                    this.nameList[i] = name;
            }
        }
        else {
            isNamePattern=false;
            this.nameList = null;
        }
    }

}
