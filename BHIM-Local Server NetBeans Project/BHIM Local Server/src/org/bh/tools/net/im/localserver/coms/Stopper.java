/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bh.tools.net.im.localserver.coms;

/**
 * Stopper is copyright Blue Husky Programming ©2016 BH-1-PS <hr/>
 *
 * For controlling events asynchronously.
 *
 * @author Kyli of Blue Husky Programming
 * @version 1.0.0 - 2016-03-19 (1.0.0) - Kyli created Stopper
 * @since 2016-03-19
 */
public interface Stopper {

    /**
     * Indicates whether the caller should stop.
     *
     * @return {@code true} iff the caller should stop.
     */
    public boolean shouldStop();
}
