/*
 * $Id: $
 */
package com.mountainminds.eclemma.internal.core.testutils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.JavaRuntime;

/**
 * Base class for test cases working on Java projects providing infrastructure
 * to setup Java projects.
 * 
 * @author  Marc R. Hoffmann
 * @version $Revision: $
 */
public abstract class JavaProjectTestBase extends TestCase {

  public static final String PROJECT_NAME = "UnitTestProject";
  
  protected IWorkspace workspace;
  protected IProject project;
  protected IJavaProject javaProject;
  
  
  public JavaProjectTestBase() {
    super();
  }

  public JavaProjectTestBase(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    workspace = ResourcesPlugin.getWorkspace();
    IWorkspaceRoot root = workspace.getRoot();
    project = root.getProject(PROJECT_NAME);
    project.create(null);
    project.open(null);
    IProjectDescription description = project.getDescription();
    description.setNatureIds(new String[]{JavaCore.NATURE_ID});
    project.setDescription(description, null);
    javaProject = JavaCore.create(project);
    javaProject.setRawClasspath(new IClasspathEntry[0], null);
    addClassPathEntry(JavaRuntime.getDefaultJREContainerEntry());
  }
  
  protected String getProjectName() {
    return getClass().getName() + '.' + getName();
  }

  protected IFolder setDefaultOutputLocation(String foldername) throws CoreException {
    IFolder folder= project.getFolder(foldername);
    folder.create(false, true, null);
    javaProject.setOutputLocation(folder.getFullPath(), null);
    return folder;
  }
  
  protected IPackageFragmentRoot createSourceFolder(String foldername) throws CoreException {
    IFolder folder = project.getFolder(foldername);
    folder.create(false, true, null);
    IPackageFragmentRoot packageRoot= javaProject.getPackageFragmentRoot(folder);
    addClassPathEntry(JavaCore.newSourceEntry(packageRoot.getPath()));
    return packageRoot;
  }
  
  public IPackageFragment createPackage(IPackageFragmentRoot fragmentRoot, String name) throws CoreException{
    return fragmentRoot.createPackageFragment(name, false, null);
  }
  
  public ICompilationUnit createCompilationUnit(IPackageFragment fragment, String name, String content) throws JavaModelException{
    return fragment.createCompilationUnit(name, content, false, null);
  }
  
  public ICompilationUnit createCompilationUnit(IPackageFragmentRoot fragmentRoot, String testsrc, String path) throws CoreException, IOException {
    IPath typepath = new Path(path);
    String pkgname = typepath.removeLastSegments(1).toString().replace('/', '.');
    IPackageFragment fragment = createPackage(fragmentRoot, pkgname);
    StringBuffer sb = new StringBuffer();
    URL url = Platform.find(Platform.getBundle("com.mountainminds.eclemma.core"), new Path(testsrc).append(typepath));
    Reader r = new InputStreamReader(url.openStream());
    int c;
    while ((c = r.read()) != -1) sb.append((char) c);
    r.close();
    return createCompilationUnit(fragment, typepath.lastSegment(), sb.toString());
  }
  
  protected void addClassPathEntry(IClasspathEntry entry) throws CoreException{
    IClasspathEntry[] oldEntries= javaProject.getRawClasspath();
    IClasspathEntry[] newEntries= new IClasspathEntry[oldEntries.length + 1];
    System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
    newEntries[oldEntries.length]= entry;
    javaProject.setRawClasspath(newEntries, null);
  }
  
  protected void tearDown() throws Exception {
    project.delete(true, true, null);
  }

}
