/*******************************************************************************
 * Copyright (c) 2006, 2011 Mountainminds GmbH & Co. KG and Contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *    
 ******************************************************************************/
package com.mountainminds.eclemma.internal.ui.coverageview;

import org.eclipse.jdt.ui.JavaElementComparator;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.TreeColumn;
import org.jacoco.core.analysis.ICounter;

import com.mountainminds.eclemma.core.CoverageTools;
import com.mountainminds.eclemma.internal.ui.coverageview.ViewSettings.ICounterMode;

/**
 * Internal sorter for the coverage view.
 */
class CoverageViewSorter extends ViewerComparator {

  private final ViewSettings settings;
  private final CoverageView view;
  private final ViewerComparator elementsorter = new JavaElementComparator();

  public CoverageViewSorter(ViewSettings settings, CoverageView view) {
    this.settings = settings;
    this.view = view;
  }

  void addColumn(final TreeColumn column, final int columnidx) {
    column.addSelectionListener(new SelectionListener() {
      public void widgetSelected(SelectionEvent e) {
        settings.toggleSortColumn(columnidx);
        setSortColumnAndDirection(column, settings.isReverseSort() ? SWT.DOWN
            : SWT.UP);
        view.refreshViewer();
      }

      public void widgetDefaultSelected(SelectionEvent e) {
      }
    });
  }

  void setSortColumnAndDirection(TreeColumn sortColumn, int direction) {
    sortColumn.getParent().setSortColumn(sortColumn);
    sortColumn.getParent().setSortDirection(direction);
  }

  public int compare(Viewer viewer, Object e1, Object e2) {
    ICounterMode mode = settings.getCounterMode();
    ICounter c1 = mode.getCounter(CoverageTools.getCoverageInfo(e1));
    ICounter c2 = mode.getCounter(CoverageTools.getCoverageInfo(e2));
    int res = 0;
    switch (settings.getSortColumn()) {
    case CoverageView.COLUMN_ELEMENT:
      res = elementsorter.compare(viewer, e1, e2);
      break;
    case CoverageView.COLUMN_RATIO:
      res = Double.compare(c1.getCoveredRatio(), c2.getCoveredRatio());
      break;
    case CoverageView.COLUMN_COVERED:
      res = (int) (c1.getCoveredCount() - c2.getCoveredCount());
      break;
    case CoverageView.COLUMN_MISSED:
      res = (int) (c1.getMissedCount() - c2.getMissedCount());
      break;
    case CoverageView.COLUMN_TOTAL:
      res = (int) (c1.getTotalCount() - c2.getTotalCount());
      break;
    }
    if (res == 0) {
      res = elementsorter.compare(viewer, e1, e2);
    } else {
      res = settings.isReverseSort() ? -res : res;
    }
    return res;
  }

}
