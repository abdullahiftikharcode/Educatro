package com.example.educatro.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.educatro.CourseDetailActivity;
import com.example.educatro.R;
import com.example.educatro.adapters.CategoryAdapter;
import com.example.educatro.adapters.CourseAdapter;
import com.example.educatro.adapters.AuthorAdapter;
import com.example.educatro.models.Author;
import com.example.educatro.models.Course;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BrowseFragment extends Fragment {

    private RecyclerView categoriesRecyclerView;
    private RecyclerView recommendedCoursesRecyclerView;
    private RecyclerView topAuthorsRecyclerView;
    private RecyclerView newReleasesRecyclerView;
    private EditText searchEditText;
    private ImageView searchIcon;
    private ProgressBar loadingProgressBar;
    private TextView errorTextView;

    private CategoryAdapter categoryAdapter;
    private CourseAdapter recommendedCoursesAdapter;
    private AuthorAdapter topAuthorsAdapter;
    private CourseAdapter newReleasesAdapter;

    private FirebaseDatabase database;
    private DatabaseReference coursesRef;
    private DatabaseReference authorsRef;
    private DatabaseReference categoriesRef;

    private String userId;

    public BrowseFragment() {
        // Required empty public constructor
    }

    public static BrowseFragment newInstance(String userId) {
        BrowseFragment fragment = new BrowseFragment();
        Bundle args = new Bundle();
        args.putString("userId", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString("userId");
        }

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        coursesRef = database.getReference("courses");
        authorsRef = database.getReference("authors");
        categoriesRef = database.getReference("categories");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_browse, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        categoriesRecyclerView = view.findViewById(R.id.categoriesRecyclerView);
        recommendedCoursesRecyclerView = view.findViewById(R.id.recommendedCoursesRecyclerView);
        topAuthorsRecyclerView = view.findViewById(R.id.topAuthorsRecyclerView);
        newReleasesRecyclerView = view.findViewById(R.id.newReleasesRecyclerView);
        searchEditText = view.findViewById(R.id.searchEditText);
        searchIcon = view.findViewById(R.id.searchIcon);
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar);
        errorTextView = view.findViewById(R.id.errorTextView);

        // Set up RecyclerViews
        setupCategoriesRecyclerView();
        setupRecommendedCoursesRecyclerView();
        setupTopAuthorsRecyclerView();
        setupNewReleasesRecyclerView();

        // Load data
        loadCategories();
        loadRecommendedCourses();
        loadTopAuthors();
        loadNewReleases();

        // Set up search
        setupSearch();
    }

    private void setupCategoriesRecyclerView() {
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryAdapter = new CategoryAdapter(new ArrayList<>());
        categoriesRecyclerView.setAdapter(categoryAdapter);
    }

    private void setupRecommendedCoursesRecyclerView() {
        recommendedCoursesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recommendedCoursesAdapter = new CourseAdapter(new ArrayList<>(), new CourseAdapter.OnCourseClickListener() {
            @Override
            public void onCourseClick(Course course) {
                navigateToCourseDetail(course);
            }
        });
        recommendedCoursesRecyclerView.setAdapter(recommendedCoursesAdapter);
    }

    private void setupTopAuthorsRecyclerView() {
        topAuthorsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        topAuthorsAdapter = new AuthorAdapter(new ArrayList<>());
        topAuthorsRecyclerView.setAdapter(topAuthorsAdapter);
    }

    private void setupNewReleasesRecyclerView() {
        newReleasesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        newReleasesAdapter = new CourseAdapter(new ArrayList<>(), new CourseAdapter.OnCourseClickListener() {
            @Override
            public void onCourseClick(Course course) {
                navigateToCourseDetail(course);
            }
        });
        newReleasesRecyclerView.setAdapter(newReleasesAdapter);
    }

    private void setupSearch() {
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle search
                String query = searchEditText.getText().toString().trim();
                // Navigate to search results screen
                Intent intent = new Intent(getActivity(), com.example.educatro.SearchActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("query", query);
                startActivity(intent);
            }
        });
    }

    private void loadCategories() {
        showLoading();
        categoriesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> categories = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String category = snapshot.getValue(String.class);
                    categories.add(category);
                }
                
                // If no categories were found, add dummy categories
                if (categories.isEmpty()) {
                    categories.add("History");
                    categories.add("Business & Management");
                    categories.add("Law");
                    categories.add("Politics & Society");
                    categories.add("Literature");
                    categories.add("Science");
                    categories.add("Technology");
                    categories.add("Arts & Culture");
                    categories.add("Health & Medicine");
                    categories.add("Languages");
                    
                    // Save dummy categories to Firebase for future use
                    for (int i = 0; i < categories.size(); i++) {
                        categoriesRef.child(String.valueOf(i)).setValue(categories.get(i));
                    }
                }
                
                categoryAdapter.setCategories(categories);
                hideLoading();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showError("Error loading categories: " + databaseError.getMessage());
            }
        });
    }

    private void loadRecommendedCourses() {
        showLoading();
        coursesRef.orderByChild("rating").limitToLast(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Course> courses = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Course course = snapshot.getValue(Course.class);
                    courses.add(course);
                }
                
                // If no courses were found, add dummy recommended courses
                if (courses.isEmpty()) {
                    // Create dummy courses with different categories
                    courses.add(createDummyCourse("rc1", "Introduction to Psychology", "Science", 
                            "Learn the basics of psychology and human behavior", "prof_smith", "Prof. Smith"));
                    courses.add(createDummyCourse("rc2", "Modern History: World War II", "History", 
                            "Comprehensive analysis of WWII events and consequences", "dr_johnson", "Dr. Johnson"));
                    courses.add(createDummyCourse("rc3", "Business Ethics", "Business & Management", 
                            "Ethical principles in modern business environments", "prof_zhang", "Prof. Zhang"));
                    courses.add(createDummyCourse("rc4", "Constitutional Law", "Law", 
                            "Study of fundamental legal principles governing state power", "judge_williams", "Judge Williams"));
                    courses.add(createDummyCourse("rc5", "Classic Literature Analysis", "Literature", 
                            "Analysis of great literary works throughout history", "prof_garcia", "Prof. Garcia"));
                    
                    // Save dummy courses to Firebase for future use
                    for (Course course : courses) {
                        coursesRef.child(course.getCourseId()).setValue(course);
                    }
                }
                
                recommendedCoursesAdapter.setCourses(courses);
                hideLoading();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showError("Error loading recommended courses: " + databaseError.getMessage());
            }
        });
    }

    private void loadTopAuthors() {
        showLoading();
        authorsRef.orderByChild("followersCount").limitToLast(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Author> authors = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Author author = snapshot.getValue(Author.class);
                    authors.add(author);
                }
                
                // If no authors were found, add dummy authors
                if (authors.isEmpty()) {
                    authors.add(createDummyAuthor("prof_smith", "Prof. Smith", "Science", 
                            "Expert in psychology with over 15 years of teaching experience"));
                    authors.add(createDummyAuthor("dr_johnson", "Dr. Johnson", "History", 
                            "History professor specializing in modern European history"));
                    authors.add(createDummyAuthor("prof_zhang", "Prof. Zhang", "Business & Management", 
                            "Business ethics expert with industry and academic experience"));
                    authors.add(createDummyAuthor("judge_williams", "Judge Williams", "Law", 
                            "Former federal judge with expertise in constitutional law"));
                    authors.add(createDummyAuthor("prof_garcia", "Prof. Garcia", "Literature", 
                            "Comparative literature professor focusing on classics"));
                    
                    // Save dummy authors to Firebase for future use
                    for (Author author : authors) {
                        authorsRef.child(author.getAuthorId()).setValue(author);
                    }
                }
                
                topAuthorsAdapter.setAuthors(authors);
                hideLoading();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showError("Error loading top authors: " + databaseError.getMessage());
            }
        });
    }

    private void loadNewReleases() {
        showLoading();
        // In a real app, you would order by timestamp
        coursesRef.limitToLast(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Course> courses = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Course course = snapshot.getValue(Course.class);
                    courses.add(course);
                }
                newReleasesAdapter.setCourses(courses);
                hideLoading();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showError("Error loading new releases: " + databaseError.getMessage());
            }
        });
    }

    private void navigateToCourseDetail(Course course) {
        Intent intent = new Intent(getContext(), CourseDetailActivity.class);
        intent.putExtra("courseId", course.getCourseId());
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    private void showLoading() {
        loadingProgressBar.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.GONE);
    }

    private void hideLoading() {
        loadingProgressBar.setVisibility(View.GONE);
    }

    private void showError(String errorMessage) {
        loadingProgressBar.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(errorMessage);
    }

    private Course createDummyCourse(String courseId, String title, String category, String description, String authorId, String authorName) {
        Course course = new Course();
        course.setCourseId(courseId);
        course.setTitle(title);
        course.setCategory(category);
        course.setDescription(description);
        course.setAuthorId(authorId);
        course.setAuthorName(authorName);
        
        // Set default values for other properties
        course.setImageUrl("https://via.placeholder.com/300x200.png?text=" + title.replaceAll("\\s+", "+"));
        course.setDuration(60 + (int)(Math.random() * 120)); // Random duration between 60-180 minutes
        course.setLessonsCount(5 + (int)(Math.random() * 10)); // Random between 5-15 lessons
        course.setRating(3.5f + (float)(Math.random() * 1.5)); // Random rating between 3.5-5.0
        course.setRatingsCount(50 + (int)(Math.random() * 200)); // Random between 50-250 ratings
        course.setPrice(9.99 + (Math.random() * 90.0)); // Random price between $9.99-$99.99
        
        // Add some skills
        List<String> skills = new ArrayList<>();
        skills.add("Beginner level");
        skills.add("Certificate available");
        skills.add("Mobile access");
        course.setSkills(skills);
        
        // Add some lessons
        List<Course.Lesson> lessons = new ArrayList<>();
        int lessonCount = course.getLessonsCount();
        for (int i = 0; i < lessonCount; i++) {
            Course.Lesson lesson = new Course.Lesson(
                    courseId + "_lesson" + i,
                    "Lesson " + (i + 1) + ": " + generateLessonTitle(title, i),
                    10 + (int)(Math.random() * 20) // 10-30 minutes per lesson
            );
            lessons.add(lesson);
        }
        course.setLessons(lessons);
        
        return course;
    }
    
    private String generateLessonTitle(String courseTitle, int lessonIndex) {
        String[] beginnerLessons = {
                "Introduction to ", "Fundamentals of ", "Getting Started with ", "Basics of ", "Understanding "
        };
        String[] intermediateLessons = {
                "Exploring ", "Analyzing ", "Working with ", "Discovering ", "Investigating "
        };
        String[] advancedLessons = {
                "Mastering ", "Advanced ", "Deep Dive into ", "Expert Techniques in ", "Professional "
        };
        
        String[][] allLessons = {beginnerLessons, intermediateLessons, advancedLessons};
        int level = Math.min(lessonIndex / 3, 2); // Progress through difficulty levels
        
        String[] currentLevel = allLessons[level];
        String prefix = currentLevel[lessonIndex % currentLevel.length];
        
        // Get a relevant subject from the course title
        String subject = courseTitle;
        if (courseTitle.contains(":")) {
            subject = courseTitle.split(":")[1].trim();
        } else if (courseTitle.contains(" to ")) {
            subject = courseTitle.split(" to ")[1].trim();
        }
        
        return prefix + subject;
    }

    private Author createDummyAuthor(String authorId, String name, String category, String bio) {
        Author author = new Author(authorId, name, 
                "https://via.placeholder.com/150.png?text=" + name.replaceAll("\\s+", "+"), 
                category, bio);
                
        // Set other properties
        author.setRating(4.0f + (float)(Math.random() * 1.0)); // Random rating between 4.0-5.0
        author.setRatingsCount(100 + (int)(Math.random() * 500)); // Random between 100-600 ratings
        author.setFollowersCount(1000 + (int)(Math.random() * 9000)); // Random between 1000-10000 followers
        author.setFollowingCount((int)(Math.random() * 100)); // Random between 0-100 following
        
        // Add social links
        List<String> socialLinks = new ArrayList<>();
        socialLinks.add("https://twitter.com/" + name.toLowerCase().replaceAll("\\s+", ""));
        socialLinks.add("https://linkedin.com/in/" + name.toLowerCase().replaceAll("\\s+", "-"));
        author.setSocialLinks(socialLinks);
        
        // Add course IDs (matching our dummy courses)
        List<String> courses = new ArrayList<>();
        if (authorId.equals("prof_smith")) courses.add("rc1");
        if (authorId.equals("dr_johnson")) courses.add("rc2");
        if (authorId.equals("prof_zhang")) courses.add("rc3");
        if (authorId.equals("judge_williams")) courses.add("rc4");
        if (authorId.equals("prof_garcia")) courses.add("rc5");
        author.setCourses(courses);
        
        return author;
    }
} 