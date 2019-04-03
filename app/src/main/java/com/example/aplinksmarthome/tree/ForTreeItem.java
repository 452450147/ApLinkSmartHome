package com.example.aplinksmarthome.tree;

import java.io.Serializable;

/**
 * Created by owant on 09/03/2017.
 */

public interface ForTreeItem<T extends NodeModel<?>> extends Serializable {
    void next(int msg, T next);
}