package com.example.educatro;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.educatro.adapters.LessonAdapter;
import com.example.educatro.adapters.SkillAdapter;
import com.example.educatro.models.Course;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CourseDetailActivity extends AppCompatActivity {

    private ImageButton backButton;
    private ImageView courseImageView;
    private TextView titleTextView;
    private TextView authorTextView;
    private RatingBar ratingBar;
    private TextView ratingCountTextView;
    private TextView lessonsCountTextView;
    private TextView durationTextView;
    private ImageButton bookmarkButton;
    private ImageButton downloadButton;
    private TabLayout tabLayout;
    private TextView descriptionTextView;
    private RecyclerView skillsRecyclerView;
    private RecyclerView lessonsRecyclerView;
    private Button enrollButton;

    private SkillAdapter skillAdapter;
    private LessonAdapter lessonAdapter;

    private FirebaseDatabase database;
    private DatabaseReference coursesRef;
    private DatabaseReference usersRef;

    private String courseId;
    private String userId;
    private Course currentCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        // Get course ID and user ID from intent
        courseId = getIntent().getStringExtra("courseId");
        userId = getIntent().getStringExtra("userId");

        if (courseId == null || userId == null) {
            finish(); // Close activity if no course ID or user ID
            return;
        }

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        coursesRef = database.getReference("courses");
        usersRef = database.getReference("users");

        // Initialize views
        backButton = findViewById(R.id.backButton);
        courseImageView = findViewById(R.id.courseImageView);
        titleTextView = findViewById(R.id.titleTextView);
        authorTextView = findViewById(R.id.authorTextView);
        ratingBar = findViewById(R.id.ratingBar);
        ratingCountTextView = findViewById(R.id.ratingCountTextView);
        lessonsCountTextView = findViewById(R.id.lessonsCountTextView);
        durationTextView = findViewById(R.id.durationTextView);
        bookmarkButton = findViewById(R.id.bookmarkButton);
        downloadButton = findViewById(R.id.downloadButton);
        tabLayout = findViewById(R.id.tabLayout);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        skillsRecyclerView = findViewById(R.id.skillsRecyclerView);
        lessonsRecyclerView = findViewById(R.id.lessonsRecyclerView);
        enrollButton = findViewById(R.id.enrollButton);

        // Set up RecyclerViews
        setupSkillsRecyclerView();
        setupLessonsRecyclerView();

        // Set up click listeners
        setupClickListeners();

        // Load course data
        loadCourseData();
    }

    private void setupSkillsRecyclerView() {
        skillsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        skillAdapter = new SkillAdapter(new ArrayList<>());
        skillsRecyclerView.setAdapter(skillAdapter);
    }

    private void setupLessonsRecyclerView() {
        lessonsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        lessonAdapter = new LessonAdapter(new ArrayList<>());
        lessonsRecyclerView.setAdapter(lessonAdapter);
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Go back
            }
        });

        bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBookmark();
            }
        });

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDownload();
            }
        });

        enrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enrollInCourse();
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                updateTabContent(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Not needed
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Not needed
            }
        });
    }

    private void loadCourseData() {
        coursesRef.child(courseId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentCourse = dataSnapshot.getValue(Course.class);
                if (currentCourse != null) {
                    displayCourseData();
                    checkUserEnrollmentStatus();
                    checkUserBookmarkStatus();
                    checkUserDownloadStatus();
                } else {
                    // Course not found, create a dummy course
                    createDummyCourse();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CourseDetailActivity.this, "Error loading course: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                // Create dummy course in case of error
                createDummyCourse();
            }
        });
    }

    private void createDummyCourse() {
        // Create a dummy course based on the courseId
        currentCourse = new Course();
        currentCourse.setCourseId(courseId);
        
        // Set default values based on course ID
        if (courseId.startsWith("rc")) {
            // This is one of our recommended courses
            switch (courseId) {
                case "rc1":
                    currentCourse.setTitle("Introduction to Psychology");
                    currentCourse.setCategory("Science");
                    currentCourse.setDescription("Learn the basics of psychology and human behavior. This course covers fundamental concepts in psychology, including cognitive processes, behavioral patterns, and psychological development.");
                    currentCourse.setAuthorId("prof_smith");
                    currentCourse.setAuthorName("Prof. Smith");
                    break;
                case "rc2":
                    currentCourse.setTitle("Modern History: World War II");
                    currentCourse.setCategory("History");
                    currentCourse.setDescription("Comprehensive analysis of WWII events and consequences. Explore the causes, major battles, political strategies, and lasting impact of World War II on global politics and society.");
                    currentCourse.setAuthorId("dr_johnson");
                    currentCourse.setAuthorName("Dr. Johnson");
                    break;
                case "rc3":
                    currentCourse.setTitle("Business Ethics");
                    currentCourse.setCategory("Business & Management");
                    currentCourse.setDescription("Ethical principles in modern business environments. Examine ethical dilemmas, corporate responsibility, sustainable business practices, and ethical leadership in today's global marketplace.");
                    currentCourse.setAuthorId("prof_zhang");
                    currentCourse.setAuthorName("Prof. Zhang");
                    break;
                case "rc4":
                    currentCourse.setTitle("Constitutional Law");
                    currentCourse.setCategory("Law");
                    currentCourse.setDescription("Study of fundamental legal principles governing state power. Analyze constitutional frameworks, landmark court cases, civil liberties, and the balance of powers in democratic systems.");
                    currentCourse.setAuthorId("judge_williams");
                    currentCourse.setAuthorName("Judge Williams");
                    break;
                case "rc5":
                    currentCourse.setTitle("Classic Literature Analysis");
                    currentCourse.setCategory("Literature");
                    currentCourse.setDescription("Analysis of great literary works throughout history. Explore themes, narrative techniques, historical context, and cultural significance of classic novels, poetry, and plays.");
                    currentCourse.setAuthorId("prof_garcia");
                    currentCourse.setAuthorName("Prof. Garcia");
                    break;
                default:
                    currentCourse.setTitle("Course " + courseId);
                    currentCourse.setCategory("General");
                    currentCourse.setDescription("This is a sample course description.");
                    currentCourse.setAuthorId("author_default");
                    currentCourse.setAuthorName("Default Author");
                    break;
            }
        } else {
            // Generic course
            currentCourse.setTitle("Course " + courseId);
            currentCourse.setCategory("General");
            currentCourse.setDescription("This is a sample course description.");
            currentCourse.setAuthorId("author_default");
            currentCourse.setAuthorName("Default Author");
        }
        
        // Set default values for other properties
        currentCourse.setImageUrl("https://via.placeholder.com/300x200.png?text=" + currentCourse.getTitle().replaceAll("\\s+", "+"));
        currentCourse.setDuration(120); // 2 hours
        currentCourse.setLessonsCount(10);
        currentCourse.setRating(4.5f);
        currentCourse.setRatingsCount(125);
        currentCourse.setPrice(29.99);
        
        // Add some skills
        List<String> skills = new ArrayList<>();
        skills.add("Beginner level");
        skills.add("Certificate available");
        skills.add("Mobile access");
        currentCourse.setSkills(skills);
        
        // Add some lessons
        List<Course.Lesson> lessons = new ArrayList<>();
        for (int i = 0; i < currentCourse.getLessonsCount(); i++) {
            Course.Lesson lesson = new Course.Lesson(
                    courseId + "_lesson" + i,
                    "Lesson " + (i + 1) + ": Sample Lesson",
                    15 // 15 minutes per lesson
            );
            lessons.add(lesson);
        }
        currentCourse.setLessons(lessons);
        
        // Save this dummy course to Firebase for future use
        coursesRef.child(courseId).setValue(currentCourse);
        
        // Display the dummy course
        displayCourseData();
        checkUserEnrollmentStatus();
        checkUserBookmarkStatus();
        checkUserDownloadStatus();
    }

    private void displayCourseData() {
        titleTextView.setText(currentCourse.getTitle());
        authorTextView.setText(currentCourse.getAuthorName());
        ratingBar.setRating(currentCourse.getRating());
        ratingCountTextView.setText(String.format("(%d)", currentCourse.getRatingsCount()));
        lessonsCountTextView.setText(String.format("%d %s", currentCourse.getLessonsCount(), getString(R.string.lessons)));
        durationTextView.setText(formatDuration(currentCourse.getDuration()));
        descriptionTextView.setText(currentCourse.getDescription());

        // Load image with Glide
        if (currentCourse.getImageUrl() != null && !currentCourse.getImageUrl().isEmpty()) {
            Glide.with(this)
                    .load(currentCourse.getImageUrl())
                    .placeholder(R.drawable.placeholder_course)
                    .error(R.drawable.placeholder_course)
                    .centerCrop()
                    .into(courseImageView);
        } else {
            courseImageView.setImageResource(R.drawable.placeholder_course);
        }

        // Set skills
        if (currentCourse.getSkills() != null) {
            skillAdapter.setSkills(currentCourse.getSkills());
        }

        // Set lessons
        if (currentCourse.getLessons() != null) {
            lessonAdapter.setLessons(currentCourse.getLessons());
        }
    }

    private String formatDuration(int minutes) {
        int hours = minutes / 60;
        int mins = minutes % 60;
        if (hours > 0) {
            return String.format("%dh %dm", hours, mins);
        } else {
            return String.format("%dm", mins);
        }
    }

    private void updateTabContent(int position) {
        if (position == 0) { // Description tab
            descriptionTextView.setVisibility(View.VISIBLE);
            skillsRecyclerView.setVisibility(View.VISIBLE);
            lessonsRecyclerView.setVisibility(View.GONE);
        } else if (position == 1) { // Content tab
            descriptionTextView.setVisibility(View.GONE);
            skillsRecyclerView.setVisibility(View.GONE);
            lessonsRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void checkUserEnrollmentStatus() {
        usersRef.child(userId).child("enrolledCourses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // User doesn't have any enrolled courses yet, create the node
                    usersRef.child(userId).child("enrolledCourses").setValue(new ArrayList<>());
                    updateEnrollButtonState(false);
                    return;
                }
                
                boolean isEnrolled = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String enrolledCourseId = snapshot.getValue(String.class);
                    if (enrolledCourseId != null && enrolledCourseId.equals(courseId)) {
                        isEnrolled = true;
                        break;
                    }
                }

                updateEnrollButtonState(isEnrolled);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CourseDetailActivity.this, "Error checking enrollment status", Toast.LENGTH_SHORT).show();
                updateEnrollButtonState(false);
            }
        });
    }

    private void checkUserBookmarkStatus() {
        usersRef.child(userId).child("bookmarkedCourses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // User doesn't have any bookmarked courses yet, create the node
                    usersRef.child(userId).child("bookmarkedCourses").setValue(new ArrayList<>());
                    updateBookmarkButtonState(false);
                    return;
                }
                
                boolean isBookmarked = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String bookmarkedCourseId = snapshot.getValue(String.class);
                    if (bookmarkedCourseId != null && bookmarkedCourseId.equals(courseId)) {
                        isBookmarked = true;
                        break;
                    }
                }

                updateBookmarkButtonState(isBookmarked);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CourseDetailActivity.this, "Error checking bookmark status", Toast.LENGTH_SHORT).show();
                updateBookmarkButtonState(false);
            }
        });
    }

    private void checkUserDownloadStatus() {
        usersRef.child(userId).child("downloadedCourses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // User doesn't have any downloaded courses yet, create the node
                    usersRef.child(userId).child("downloadedCourses").setValue(new ArrayList<>());
                    updateDownloadButtonState(false);
                    return;
                }
                
                boolean isDownloaded = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String downloadedCourseId = snapshot.getValue(String.class);
                    if (downloadedCourseId != null && downloadedCourseId.equals(courseId)) {
                        isDownloaded = true;
                        break;
                    }
                }

                updateDownloadButtonState(isDownloaded);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CourseDetailActivity.this, "Error checking download status", Toast.LENGTH_SHORT).show();
                updateDownloadButtonState(false);
            }
        });
    }

    private void updateEnrollButtonState(boolean isEnrolled) {
        if (isEnrolled) {
            enrollButton.setText(R.string.continue_learning);
            enrollButton.setEnabled(true);
        } else {
            enrollButton.setText(R.string.start_free_trial);
            enrollButton.setEnabled(true);
        }
    }

    private void updateBookmarkButtonState(boolean isBookmarked) {
        bookmarkButton.setImageResource(isBookmarked ? R.drawable.ic_bookmark_filled : R.drawable.ic_bookmark);
        currentCourse.setBookmarked(isBookmarked);
    }

    private void updateDownloadButtonState(boolean isDownloaded) {
        downloadButton.setImageResource(isDownloaded ? R.drawable.ic_downloaded : R.drawable.ic_download);
        currentCourse.setDownloaded(isDownloaded);
    }

    private void toggleBookmark() {
        boolean newBookmarkState = !currentCourse.isBookmarked();
        
        // Update UI immediately for responsiveness
        updateBookmarkButtonState(newBookmarkState);
        
        // Update in Firebase
        if (newBookmarkState) {
            // Add to bookmarks
            usersRef.child(userId).child("bookmarkedCourses").push().setValue(courseId)
                    .addOnSuccessListener(aVoid -> Toast.makeText(CourseDetailActivity.this, "Added to bookmarks", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> {
                        // Revert UI on failure
                        updateBookmarkButtonState(!newBookmarkState);
                        Toast.makeText(CourseDetailActivity.this, "Failed to bookmark: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            // Remove from bookmarks
            usersRef.child(userId).child("bookmarkedCourses").orderByValue().equalTo(courseId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                snapshot.getRef().removeValue()
                                        .addOnSuccessListener(aVoid -> Toast.makeText(CourseDetailActivity.this, "Removed from bookmarks", Toast.LENGTH_SHORT).show())
                                        .addOnFailureListener(e -> {
                                            // Revert UI on failure
                                            updateBookmarkButtonState(!newBookmarkState);
                                            Toast.makeText(CourseDetailActivity.this, "Failed to remove bookmark: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Revert UI on failure
                            updateBookmarkButtonState(!newBookmarkState);
                            Toast.makeText(CourseDetailActivity.this, "Failed to remove bookmark: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void toggleDownload() {
        boolean newDownloadState = !currentCourse.isDownloaded();
        
        // Update UI immediately for responsiveness
        updateDownloadButtonState(newDownloadState);
        
        // Update in Firebase
        if (newDownloadState) {
            // Add to downloads
            usersRef.child(userId).child("downloadedCourses").push().setValue(courseId)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(CourseDetailActivity.this, "Course downloaded for offline use", Toast.LENGTH_SHORT).show();
                        // In a real app, you would actually download the course content here
                    })
                    .addOnFailureListener(e -> {
                        // Revert UI on failure
                        updateDownloadButtonState(!newDownloadState);
                        Toast.makeText(CourseDetailActivity.this, "Failed to download: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            // Remove from downloads
            usersRef.child(userId).child("downloadedCourses").orderByValue().equalTo(courseId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                snapshot.getRef().removeValue()
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(CourseDetailActivity.this, "Course removed from downloads", Toast.LENGTH_SHORT).show();
                                            // In a real app, you would delete the downloaded content here
                                        })
                                        .addOnFailureListener(e -> {
                                            // Revert UI on failure
                                            updateDownloadButtonState(!newDownloadState);
                                            Toast.makeText(CourseDetailActivity.this, "Failed to remove download: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Revert UI on failure
                            updateDownloadButtonState(!newDownloadState);
                            Toast.makeText(CourseDetailActivity.this, "Failed to remove download: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void enrollInCourse() {
        // Check if already enrolled
        usersRef.child(userId).child("enrolledCourses").orderByValue().equalTo(courseId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            // Not enrolled yet, add to enrolled courses
                            usersRef.child(userId).child("enrolledCourses").push().setValue(courseId)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(CourseDetailActivity.this, "Enrolled in course", Toast.LENGTH_SHORT).show();
                                        updateEnrollButtonState(true);
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(CourseDetailActivity.this, "Failed to enroll: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                        } else {
                            // Already enrolled, start learning
                            Toast.makeText(CourseDetailActivity.this, "Continue learning", Toast.LENGTH_SHORT).show();
                            // In a real app, you would navigate to the course content/player here
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(CourseDetailActivity.this, "Error checking enrollment: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
} 