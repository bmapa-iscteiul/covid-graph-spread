package com.covidgraphspreadv2;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevObject;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

public class app {

	public static Git git;
	
	public static void main(String[] args) {
		cloneRepository();
		getAllFilesFromTags();
	}
	
	public static void getAllFilesFromTags() {
		Repository repository = git.getRepository();
		try {
			//gets the list of all tags
			List<Ref> call = git.tagList().call();
			
			for (Ref ref : call) {
				System.out.println("Tag: " + ref.getName());
				RevWalk walk = new RevWalk(repository);
				try {
					RevObject object = walk.parseAny(ref.getObjectId());
					
					//gets the commit associated to the tag
					if (object instanceof RevCommit) {
						System.out.println("RevCommit: " + ref.getName());
						//retrieves file from commit using the commit ObjectId
						getFileFromCommit(ref.getObjectId());
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void getFileFromCommit(ObjectId commitId) {
		Repository repository = git.getRepository();
	
		try(RevWalk revWalk = new RevWalk(repository)){
			RevCommit commit = revWalk.parseCommit(commitId);
			RevTree tree = commit.getTree();
			System.out.println("Tree: " + tree);
			
			try(TreeWalk treeWalk = new TreeWalk(repository)){
				treeWalk.addTree(tree);
				treeWalk.setRecursive(true);
				treeWalk.setFilter(PathFilter.create("covid19spreading.rdf"));
				if(!treeWalk.next()) {
					throw new IllegalStateException("Didnt find file");
				}
				
				ObjectId objectId = treeWalk.getObjectId(0);
				ObjectLoader loader = repository.open(objectId);
				
				loader.copyTo(System.out);
			}
			revWalk.dispose();
		} catch (MissingObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IncorrectObjectTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void cloneRepository() {
		File f = new File("/path/to/repo");
		if (f.exists() && f.isDirectory()) {
			try {
				git = Git.open(new File("/path/to/repo/.git"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				git = Git.cloneRepository()
						.setURI("https://github.com/vbasto-iscte/ESII1920")
						.setDirectory(new File("/path/to/repo"))
						.call();
			} catch (InvalidRemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransportException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (GitAPIException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void createHTMLTable() {
		
	}
}

