package com.test.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.test.bean.Blog;
import com.test.bean.Comment;
import com.test.bean.Info;
import com.test.bean.Type;
import com.test.service.IBlogService;
import com.test.service.ICommentService;
import com.test.service.IInfoService;
import com.test.service.ITypeService;

@Controller
public class BlogController {
	//�ĸ�spring��ǩע���service
	@Resource
	private IBlogService blogService;
	
	@Resource
	private ITypeService typeService;
	
	@Resource
	private ICommentService commentService;
	
	@Resource
	private IInfoService infoService;

	//һ�����ڹ�����ÿҳ�����ı���
	private int pageCount=6;
	
	
	//��ҳ��ʾ
	@RequestMapping("index")
	public String toIndex(Model model){
		//��ҳȡ��ҳ�ĵ�һҳ����
		int pageNum=1;
		List<Blog> blogs=this.blogService.getAllBlogs();
		int allpages=(blogs.size()/pageCount)+(blogs.size()%pageCount==0?0:1);
		//��������ԭ��Ϊ���԰������ڴ��µ������У�
		// û�뵽û��order by������
		Collections.reverse(blogs);
		//������blog��ȡ����һҳ���ݣ���������mysql��sql���ʵ�ַ�ҳ��������Ŀǰ��ʵ�ַ�ʽ���ã��Ȳ��Ż���
		blogs=blogs.subList((pageNum-1)*pageCount, pageNum*pageCount>blogs.size()?blogs.size():pageNum*pageCount);
		//��ȡ��������������빩��ͼ��Ⱦʹ��
		List<Type> types=this.typeService.getTypes();
		//�õ���վ��ز�����������ʲô��
		Info info=this.infoService.getInfoById(1);

		//������Ҫ�õ��ļ���model
		model.addAttribute("info", info);
		model.addAttribute("blogs",blogs);
		model.addAttribute("types",types);
		model.addAttribute("pagenum",pageNum);
		model.addAttribute("allpages",allpages);
		return "index";
		
	}
	
	@RequestMapping("searchByType/{id}")
	public String toSearchByType(Model model,@PathVariable("id") Integer id){
		//��ȡ��������������빩��ͼ��Ⱦʹ��
		List<Type> types=this.typeService.getTypes();
		//��������id�Ҳ�������
		Type type=new Type();
		type.setId(id);
		List<Blog> blogs=this.blogService.findBlogsByType(type);
		//�õ���վ��ز�����������ʲô��
		Info info=this.infoService.getInfoById(1);
		//������Ҫ�õ��ļ���model
		model.addAttribute("info", info);
		model.addAttribute("types",types);
		model.addAttribute("blogs",blogs);
		return "searchByType";		
	}
	
	@RequestMapping("searchByWord")
	public String toSearchByWord(Model model,HttpServletRequest request) throws UnsupportedEncodingException{
		//��֪Ϊ�β�����Ϣ����������ʱ�����������룬û�ҵ�ԭ�򣬾���ô�����
		String word=new String(request.getParameter("word").getBytes("ISO-8859-1"),"UTF-8");
		//���ݹؼ��ʲ������£���������ݾ���
		List<Blog> blogs=this.blogService.findBlogsByWord(word);
		//��ȡ��������������빩��ͼ��Ⱦʹ��
		List<Type> types=this.typeService.getTypes();
		//�õ���վ��ز�����������ʲô��
		Info info=this.infoService.getInfoById(1);

		//������Ҫ�õ��ļ���model
		model.addAttribute("blogs",blogs);	
		model.addAttribute("types",types);
		model.addAttribute("info", info);
		return "index";		
	}
	
	/*
	@RequestMapping("showBlog/{id}")
	public String toOneBlog(HttpServletRequest request,Model model,@PathVariable("id") Integer id){
		List<Type> types=this.typeService.getTypes();
		model.addAttribute("types",types);
		Blog blog=this.blogService.getBlogByIdWithComment(id);
		model.addAttribute("blog",blog);
		return "showBlog";
		
	}*/
	//������ʾĳƪ����
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public String toOneBlog(HttpServletRequest request,Model model,@PathVariable("id") Integer id){
		//��ȡ��������������빩��ͼ��Ⱦʹ��
		List<Type> types=this.typeService.getTypes();
		//��ø���id��ƪ����
		Blog blog=this.blogService.getBlogById(id);
		//����һ�ε����
		blog.setClickTimes(blog.getClickTimes() + 1);
		//�����������
		List<Comment> comments=commentService.selectByBlogId(blog.getId());
		//ˢ��һ�β���������
		blog.setCommentTimes(comments.size());
		this.blogService.updateBlog(blog);
		//�õ���վ��ز�����������ʲô��
		Info info=this.infoService.getInfoById(1);

		//������Ҫ�õ��ļ���model
		model.addAttribute("types",types);
		model.addAttribute("blog",blog);
		model.addAttribute("comments", comments);
		model.addAttribute("info", info);
		return "showBlog";
		
	}

	//ɾ��ĳƪ����
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	public String deleteOneBlog(HttpServletRequest request,Model model,@PathVariable("id") Integer id){
		//�����֤
		HttpSession session=request.getSession(); 
		String username=(String)session.getAttribute("username");
		if(username!=null){
			//����IDɾ����ز��ͺ�����
			blogService.deleteBlogById(id);
			//ɾ�������ַ�ʽ�����ã������������blogidɾ���۵�sql���
			List<Type> types=typeService.getTypes();
			for (int i = 0; i < types.size(); i++) {
				if(blogService.findBlogsByType(types.get(i))==null||blogService.findBlogsByType(types.get(i)).size()<1){
					typeService.deleteTypeById(types.get(i).getId());
				}
				
			}
		}
		return "redirect:/manager";	
		
	}
	//�޸�ĳƪ����
	@RequestMapping(value="/{id}",method=RequestMethod.PUT)
	public String updateOneBlog(HttpServletRequest request,Model model,@PathVariable("id") Integer id){
		//�����ƪ����
		Blog blog=blogService.getBlogById(id);
		//�����֤
		HttpSession session=request.getSession();
		String username=(String)session.getAttribute("username");
		if(username!=null){
			//�õ����µ�������ز�������������
			String title=request.getParameter("title");
			String content=request.getParameter("content");
			String typename=request.getParameter("typename");
			//���޸������������Ͳ��������½��������ڣ���ȡ��
			Type type=typeService.getTypeByName(typename);
			if (type==null) {
				type=new Type();
				type.setTypename(typename);
				typeService.addType(type);
				type=typeService.getTypeByName(typename);
			}
			blog.setTitle(title);
			blog.setContent(content);
			blog.setType(type);
			blogService.updateBlog(blog);
			//ɾȥ�������޲��͵�����
			List<Type> types=typeService.getTypes();
			for (int i = 0; i < types.size(); i++) {
				if(blogService.findBlogsByType(types.get(i))==null||blogService.findBlogsByType(types.get(i)).size()<1){
					typeService.deleteTypeById(types.get(i).getId());
				}
				
			}
		}				
		return "redirect:/manager";	
	}
	
	//���ͷ�ҳ�������������ҳдһ�������ĺ���������϶�
	@RequestMapping(value="/page/{pageNum}",method=RequestMethod.GET)
	public String getBlogByPage(HttpServletRequest request,Model model,@PathVariable("pageNum") Integer pageNum){

		//��ȡ��������������빩��ͼ��Ⱦʹ��
		List<Type> types=this.typeService.getTypes();
		//���õ����в����ٸ��ݵ�ǰҳѡȡ
		List<Blog> blogs=this.blogService.getAllBlogs();
		int allpages=(blogs.size()/pageCount)+(blogs.size()%pageCount==0?0:1);
		Collections.reverse(blogs);
		blogs=blogs.subList((pageNum-1)*pageCount, pageNum*pageCount>blogs.size()?blogs.size():pageNum*pageCount);
		//������Ҫ�õ��ļ���model
		model.addAttribute("blogs",blogs);
		model.addAttribute("types",types);
		model.addAttribute("pagenum",pageNum);
		model.addAttribute("allpages",allpages);
		return "index";	
	}
	//��������
	@RequestMapping(value="/post",method=RequestMethod.POST)
	public String addOneBlog(HttpServletRequest request,Model model) throws IOException{
		//�����֤
		HttpSession session=request.getSession();
		String username=(String)session.getAttribute("username");
		if(username!=null){
			//��ȡ��ز���
			String title=request.getParameter("title");
			//System.out.println(title);
			String typename=request.getParameter("typename");
			String content=request.getParameter("content");
			//���޸������������Ͳ��������½��������ڣ���ȡ��
			Type type=typeService.getTypeByName(typename);
			if (type==null) {
				type=new Type();
				type.setTypename(typename);
				typeService.addType(type);
				type=typeService.getTypeByName(typename);
			}
			//��������ʵ��
			Blog blog=new Blog();
			blog.setAgreeWithTimes(0);
			blog.setClickTimes(0);
			blog.setCommentTimes(0);
			blog.setContent(content);
			blog.setCreateTime(new Date());
			blog.setTitle(title);
			blog.setType(type);
			Boolean flag=blogService.insert(blog);
			//System.out.println(flag);
			
		}
		return "redirect:/manager";	
	}
	//json���ݲ�����
	@RequestMapping("/xmlOrJson")
	@ResponseBody
	public Map<String, Object> xmlOrJson() {
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("list", blogService.getAllBlogs());
	    return map;
	}
	//json���ݲ�����
	@RequestMapping("/blogJson/{id}")
	@ResponseBody
	public Map<String, Object> blogJson(@PathVariable("id") Integer id) {
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("list", blogService.getBlogById(id));
	    return map;
	}
	//���޲����ص�������json��ʽ��
	@RequestMapping(value="/addagree/{id}")
	@ResponseBody
	public Map<String, Object> addagree(HttpServletRequest request,@PathVariable("id") Integer id){
		Map<String, Object> map = new HashMap<String, Object>();
		Blog blog=blogService.getBlogById(id);
		blog.setAgreeWithTimes(blog.getAgreeWithTimes()+1);
		blogService.updateBlog(blog);
		map.put("agreetimes", blog.getAgreeWithTimes());
		return map;
	}
	//���·����ϴ�
	@RequestMapping(value="/imageupload/{id}",method=RequestMethod.POST)
	public String imageupload(MultipartFile file,HttpServletRequest request,Model model,HttpSession session,@PathVariable("id") Integer id) throws IOException{
		//����id�õ���ƪ����
		Blog blog=blogService.getBlogById(id);
		//��¼��֤
		String username=(String)session.getAttribute("username");
		if(username!=null){
			if (file!=null) {
				//�õ��ϴ������ļ���
				String fileName=file.getOriginalFilename();
				//�õ���׺��
				String type=fileName.indexOf(".")!=-1?fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()):null;
				//����׺��Ϊgif��png��jpg����Ϊ��ͼƬ���������
				if(type!=null){
					if("GIF".equals(type.toUpperCase())||"PNG".equals(type.toUpperCase())||"JPG".equals(type.toUpperCase())){
						//ͼƬ�洢·��
						String savepath=session.getServletContext().getRealPath("/")+"img"+File.separator;
						//ͼƬ�����Ƽ��洢·��
						File tempFile = new File(savepath, new Date().getTime() + "" + (int)(Math.random()*1000)+"."+type);
						//��·�������ڣ��򴴽�
						if (!tempFile.getParentFile().exists()) {
							tempFile.getParentFile().mkdir();
						}
						//���ļ������ڣ��򴴽�
						if (!tempFile.exists()) {
					    	tempFile.createNewFile();
					    }
						//��ͼƬ���ݴ����½����ļ���
						file.transferTo(tempFile);
						//������ͼƬ·���趨��blog������
					    blog.setPicUrl("/blog/img/"+tempFile.getName());
					    blogService.updateBlog(blog);
					}
				}
			}
		}
		
		return "redirect:/manager";
		
		
	}

	public IBlogService getBlogService() {
		return blogService;
	}

	public void setBlogService(IBlogService blogService) {
		this.blogService = blogService;
	}

	public ITypeService getTypeService() {
		return typeService;
	}

	public void setTypeService(ITypeService typeService) {
		this.typeService = typeService;
	}
	
}
