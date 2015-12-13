/*
 * Copyright 2012-2015 CodeLibs Project and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.codelibs.fess.es.config.exentity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codelibs.core.lang.StringUtil;
import org.codelibs.fess.es.config.bsentity.BsElevateWord;
import org.codelibs.fess.es.config.exbhv.LabelTypeBhv;
import org.codelibs.fess.es.config.exbhv.WebConfigToLabelBhv;
import org.codelibs.fess.util.ComponentUtil;
import org.dbflute.cbean.result.ListResultBean;

/**
 * @author ESFlute (using FreeGen)
 */
public class ElevateWord extends BsElevateWord {

    private static final long serialVersionUID = 1L;

    private String[] labelTypeIds;

    private List<LabelType> labelTypeList;

    /* (non-Javadoc)
     * @see org.codelibs.fess.db.exentity.CrawlingConfig#getLabelTypeIds()
     */
    public String[] getLabelTypeIds() {
        if (labelTypeIds == null) {
            return StringUtil.EMPTY_STRINGS;
        }
        return labelTypeIds;
    }

    public void setLabelTypeIds(final String[] labelTypeIds) {
        this.labelTypeIds = labelTypeIds;
    }

    public List<LabelType> getLabelTypeList() {
        if (labelTypeList == null) {
            synchronized (this) {
                if (labelTypeList == null) {
                    final WebConfigToLabelBhv webConfigToLabelBhv = ComponentUtil.getComponent(WebConfigToLabelBhv.class);
                    final ListResultBean<WebConfigToLabel> mappingList = webConfigToLabelBhv.selectList(cb -> {
                        cb.query().setWebConfigId_Equal(getId());
                        cb.specify().columnLabelTypeId();
                    });
                    final List<String> labelIdList = new ArrayList<>();
                    for (final WebConfigToLabel mapping : mappingList) {
                        labelIdList.add(mapping.getLabelTypeId());
                    }
                    final LabelTypeBhv labelTypeBhv = ComponentUtil.getComponent(LabelTypeBhv.class);
                    labelTypeList = labelIdList.isEmpty() ? Collections.emptyList() : labelTypeBhv.selectList(cb -> {
                        cb.query().setId_InScope(labelIdList);
                        cb.query().addOrderBy_SortOrder_Asc();
                    });
                }
            }
        }
        return labelTypeList;
    }

    public String[] getLabelTypeValues() {
        final List<LabelType> list = getLabelTypeList();
        final List<String> labelValueList = new ArrayList<>(list.size());
        for (final LabelType labelType : list) {
            labelValueList.add(labelType.getValue());
        }
        return labelValueList.toArray(new String[labelValueList.size()]);
    }

    public String getId() {
        return asDocMeta().id();
    }

    public void setId(final String id) {
        asDocMeta().id(id);
    }

    public Long getVersionNo() {
        return asDocMeta().version();
    }

    public void setVersionNo(final Long version) {
        asDocMeta().version(version);
    }
}