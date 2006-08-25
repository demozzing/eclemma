/*
 * $Id$
 */
package com.mountainminds.eclemma.internal.core.instr;

import org.eclipse.core.runtime.IPath;

import com.mountainminds.eclemma.core.IClassFiles;
import com.mountainminds.eclemma.core.IInstrumentation;

/**
 * Implementation of {@link IInstrumentation}.
 * 
 * @author Marc R. Hoffmann
 * @version $Revision$
 */
public class Instrumentation implements IInstrumentation {

  private final IClassFiles classfiles;
  private final boolean inplace;
  private final IPath outputlocation;
  private final IPath metadatafile;

  public Instrumentation(IClassFiles classfiles, boolean inplace,
      IPath outputlocation, IPath metadatafile) {
    this.classfiles = classfiles;
    this.inplace = inplace;
    this.outputlocation = outputlocation;
    this.metadatafile = metadatafile;
  }

  public boolean isInplace() {
    return inplace;
  }

  public IClassFiles getClassFiles() {
    return classfiles;
  }

  public IPath getOutputLocation() {
    return outputlocation;
  }

  public IPath getMetaDataFile() {
    return metadatafile;
  }

}
