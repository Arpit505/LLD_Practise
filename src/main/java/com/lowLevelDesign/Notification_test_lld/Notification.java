package main.java.com.lowLevelDesign.Notification_test_lld;
// ============== ENUMS ==============

enum NotificationType {
    EMAIL, SMS, PUSH, IN_APP
}

enum NotificationStatus {
    PENDING, SENT, FAILED, RETRY
}

enum Priority {
    LOW, MEDIUM, HIGH, URGENT
}

// ============== CORE MODELS ==============

class User {
    private String userId;
    private String email;
    private String phoneNumber;
    private String deviceToken;
    private boolean emailEnabled;
    private boolean smsEnabled;
    private boolean pushEnabled;

    public User(String userId, String email, String phoneNumber, String deviceToken) {
        this.userId = userId;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.deviceToken = deviceToken;
        this.emailEnabled = true;
        this.smsEnabled = true;
        this.pushEnabled = true;
    }

    // Getters and setters
    public String getUserId() { return userId; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getDeviceToken() { return deviceToken; }
    public boolean isEmailEnabled() { return emailEnabled; }
    public boolean isSmsEnabled() { return smsEnabled; }
    public boolean isPushEnabled() { return pushEnabled; }

    public void setEmailEnabled(boolean emailEnabled) { this.emailEnabled = emailEnabled; }
    public void setSmsEnabled(boolean smsEnabled) { this.smsEnabled = smsEnabled; }
    public void setPushEnabled(boolean pushEnabled) { this.pushEnabled = pushEnabled; }
}

class Notification {
    private String id;
    private String userId;
    private NotificationType type;
    private String title;
    private String message;
    private NotificationStatus status;
    private Priority priority;
    private long createdAt;
    private long sentAt;
    private int retryCount;
    private String metadata; // JSON string for additional data

    public Notification(String id, String userId, NotificationType type,
                        String title, String message, Priority priority) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.title = title;
        this.message = message;
        this.priority = priority;
        this.status = NotificationStatus.PENDING;
        this.createdAt = System.currentTimeMillis();
        this.retryCount = 0;
    }

    // Getters and setters
    public String getId() { return id; }
    public String getUserId() { return userId; }
    public NotificationType getType() { return type; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public NotificationStatus getStatus() { return status; }
    public Priority getPriority() { return priority; }
    public long getCreatedAt() { return createdAt; }
    public long getSentAt() { return sentAt; }
    public int getRetryCount() { return retryCount; }
    public String getMetadata() { return metadata; }

    public void setStatus(NotificationStatus status) { this.status = status; }
    public void setSentAt(long sentAt) { this.sentAt = sentAt; }
    public void incrementRetryCount() { this.retryCount++; }
    public void setMetadata(String metadata) { this.metadata = metadata; }
}

// ============== INTERFACES ==============

interface NotificationSender {
    boolean send(Notification notification, User user);
}

interface NotificationRepository {
    void save(Notification notification);
    Notification findById(String id);
    List<Notification> findByUserId(String userId);
    List<Notification> findByStatus(NotificationStatus status);
    void update(Notification notification);
}

interface UserRepository {
    User findById(String userId);
    void save(User user);
    void update(User user);
}

interface NotificationQueue {
    void enqueue(Notification notification);
    Notification dequeue();
    boolean isEmpty();
    int size();
}

// ============== CONCRETE IMPLEMENTATIONS ==============

class EmailSender implements NotificationSender {
    @Override
    public boolean send(Notification notification, User user) {
        if (!user.isEmailEnabled() || user.getEmail() == null) {
            return false;
        }

        try {
            // Simulate email sending logic
            System.out.println("Sending email to: " + user.getEmail());
            System.out.println("Subject: " + notification.getTitle());
            System.out.println("Body: " + notification.getMessage());

            // Simulate network call delay
            Thread.sleep(100);

            // Simulate 90% success rate
            return Math.random() > 0.1;
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            return false;
        }
    }
}

class SMSSender implements NotificationSender {
    @Override
    public boolean send(Notification notification, User user) {
        if (!user.isSmsEnabled() || user.getPhoneNumber() == null) {
            return false;
        }

        try {
            // Simulate SMS sending logic
            System.out.println("Sending SMS to: " + user.getPhoneNumber());
            System.out.println("Message: " + notification.getMessage());

            Thread.sleep(50);
            return Math.random() > 0.05; // 95% success rate
        } catch (Exception e) {
            System.err.println("Failed to send SMS: " + e.getMessage());
            return false;
        }
    }
}

class PushNotificationSender implements NotificationSender {
    @Override
    public boolean send(Notification notification, User user) {
        if (!user.isPushEnabled() || user.getDeviceToken() == null) {
            return false;
        }

        try {
            // Simulate push notification sending
            System.out.println("Sending push notification to device: " + user.getDeviceToken());
            System.out.println("Title: " + notification.getTitle());
            System.out.println("Message: " + notification.getMessage());

            Thread.sleep(30);
            return Math.random() > 0.02; // 98% success rate
        } catch (Exception e) {
            System.err.println("Failed to send push notification: " + e.getMessage());
            return false;
        }
    }
}

class InMemoryNotificationRepository implements NotificationRepository {
    private Map<String, Notification> notifications = new ConcurrentHashMap<>();

    @Override
    public void save(Notification notification) {
        notifications.put(notification.getId(), notification);
    }

    @Override
    public Notification findById(String id) {
        return notifications.get(id);
    }

    @Override
    public List<Notification> findByUserId(String userId) {
        return notifications.values().stream()
                .filter(n -> n.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Notification> findByStatus(NotificationStatus status) {
        return notifications.values().stream()
                .filter(n -> n.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public void update(Notification notification) {
        notifications.put(notification.getId(), notification);
    }
}

class InMemoryUserRepository implements UserRepository {
    private Map<String, User> users = new ConcurrentHashMap<>();

    @Override
    public User findById(String userId) {
        return users.get(userId);
    }

    @Override
    public void save(User user) {
        users.put(user.getUserId(), user);
    }

    @Override
    public void update(User user) {
        users.put(user.getUserId(), user);
    }
}

class PriorityNotificationQueue implements NotificationQueue {
    private PriorityQueue<Notification> queue = new PriorityQueue<>((n1, n2) -> {
        // Higher priority first, then by creation time
        int priorityCompare = n2.getPriority().ordinal() - n1.getPriority().ordinal();
        if (priorityCompare != 0) return priorityCompare;
        return Long.compare(n1.getCreatedAt(), n2.getCreatedAt());
    });

    @Override
    public synchronized void enqueue(Notification notification) {
        queue.offer(notification);
    }

    @Override
    public synchronized Notification dequeue() {
        return queue.poll();
    }

    @Override
    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public synchronized int size() {
        return queue.size();
    }
}

// ============== CORE SERVICE CLASSES ==============

class NotificationSenderFactory {
    private Map<NotificationType, NotificationSender> senders = new HashMap<>();

    public NotificationSenderFactory() {
        senders.put(NotificationType.EMAIL, new EmailSender());
        senders.put(NotificationType.SMS, new SMSSender());
        senders.put(NotificationType.PUSH, new PushNotificationSender());
        // IN_APP notifications might be handled differently
    }

    public NotificationSender getSender(NotificationType type) {
        return senders.get(type);
    }
}

class NotificationProcessor {
    private NotificationSenderFactory senderFactory;
    private UserRepository userRepository;
    private NotificationRepository notificationRepository;
    private static final int MAX_RETRY_COUNT = 3;

    public NotificationProcessor(NotificationSenderFactory senderFactory,
                                 UserRepository userRepository,
                                 NotificationRepository notificationRepository) {
        this.senderFactory = senderFactory;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
    }

    public boolean processNotification(Notification notification) {
        User user = userRepository.findById(notification.getUserId());
        if (user == null) {
            notification.setStatus(NotificationStatus.FAILED);
            notificationRepository.update(notification);
            return false;
        }

        NotificationSender sender = senderFactory.getSender(notification.getType());
        if (sender == null) {
            notification.setStatus(NotificationStatus.FAILED);
            notificationRepository.update(notification);
            return false;
        }

        boolean success = sender.send(notification, user);

        if (success) {
            notification.setStatus(NotificationStatus.SENT);
            notification.setSentAt(System.currentTimeMillis());
        } else {
            notification.incrementRetryCount();
            if (notification.getRetryCount() >= MAX_RETRY_COUNT) {
                notification.setStatus(NotificationStatus.FAILED);
            } else {
                notification.setStatus(NotificationStatus.RETRY);
            }
        }

        notificationRepository.update(notification);
        return success;
    }
}

class NotificationWorker implements Runnable {
    private NotificationQueue queue;
    private NotificationProcessor processor;
    private volatile boolean running = true;

    public NotificationWorker(NotificationQueue queue, NotificationProcessor processor) {
        this.queue = queue;
        this.processor = processor;
    }

    @Override
    public void run() {
        while (running) {
            try {
                if (!queue.isEmpty()) {
                    Notification notification = queue.dequeue();
                    if (notification != null) {
                        processor.processNotification(notification);
                    }
                } else {
                    Thread.sleep(100); // Sleep when queue is empty
                }
            } catch (Exception e) {
                System.err.println("Error processing notification: " + e.getMessage());
            }
        }
    }

    public void stop() {
        running = false;
    }
}

// ============== MAIN SERVICE CLASS ==============

class NotificationService {
    private NotificationQueue queue;
    private NotificationRepository notificationRepository;
    private UserRepository userRepository;
    private NotificationProcessor processor;
    private List<Thread> workerThreads;
    private List<NotificationWorker> workers;
    private static final int WORKER_COUNT = 5;

    public NotificationService() {
        this.queue = new PriorityNotificationQueue();
        this.notificationRepository = new InMemoryNotificationRepository();
        this.userRepository = new InMemoryUserRepository();
        this.processor = new NotificationProcessor(
                new NotificationSenderFactory(),
                userRepository,
                notificationRepository
        );

        this.workers = new ArrayList<>();
        this.workerThreads = new ArrayList<>();

        startWorkers();
    }

    private void startWorkers() {
        for (int i = 0; i < WORKER_COUNT; i++) {
            NotificationWorker worker = new NotificationWorker(queue, processor);
            Thread thread = new Thread(worker, "NotificationWorker-" + i);

            workers.add(worker);
            workerThreads.add(thread);
            thread.start();
        }
    }

    public String sendNotification(String userId, NotificationType type,
                                   String title, String message, Priority priority) {
        String notificationId = generateId();
        Notification notification = new Notification(notificationId, userId, type, title, message, priority);

        notificationRepository.save(notification);
        queue.enqueue(notification);

        return notificationId;
    }

    public void registerUser(String userId, String email, String phoneNumber, String deviceToken) {
        User user = new User(userId, email, phoneNumber, deviceToken);
        userRepository.save(user);
    }

    public List<Notification> getUserNotifications(String userId) {
        return notificationRepository.findByUserId(userId);
    }

    public Notification getNotificationStatus(String notificationId) {
        return notificationRepository.findById(notificationId);
    }

    public void updateUserPreferences(String userId, boolean emailEnabled,
                                      boolean smsEnabled, boolean pushEnabled) {
        User user = userRepository.findById(userId);
        if (user != null) {
            user.setEmailEnabled(emailEnabled);
            user.setSmsEnabled(smsEnabled);
            user.setPushEnabled(pushEnabled);
            userRepository.update(user);
        }
    }

    public void shutdown() {
        workers.forEach(NotificationWorker::stop);
        workerThreads.forEach(thread -> {
            try {
                thread.join(1000);
            } catch (InterruptedException e) {
                thread.interrupt();
            }
        });
    }

    private String generateId() {
        return "NOTIF_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }
}

// ============== DEMO/TEST CLASS ==============

class NotificationSystemDemo {
    public static void main(String[] args) throws InterruptedException {
        NotificationService service = new NotificationService();

        // Register users
        service.registerUser("user1", "user1@example.com", "+1234567890", "device_token_1");
        service.registerUser("user2", "user2@example.com", "+1234567891", "device_token_2");

        // Send notifications
        String emailNotifId = service.sendNotification("user1", NotificationType.EMAIL,
                "Welcome!", "Welcome to our platform!", Priority.MEDIUM);

        String smsNotifId = service.sendNotification("user1", NotificationType.SMS,
                "Alert", "Your account has been verified", Priority.HIGH);

        String pushNotifId = service.sendNotification("user2", NotificationType.PUSH,
                "New Message", "You have a new message", Priority.LOW);

        // Wait for processing
        Thread.sleep(2000);

        // Check status
        System.out.println("\n=== Notification Status ===");
        System.out.println("Email Status: " + service.getNotificationStatus(emailNotifId).getStatus());
        System.out.println("SMS Status: " + service.getNotificationStatus(smsNotifId).getStatus());
        System.out.println("Push Status: " + service.getNotificationStatus(pushNotifId).getStatus());

        // Get user notifications
        System.out.println("\n=== User1 Notifications ===");
        service.getUserNotifications("user1").forEach(n ->
                System.out.println("ID: " + n.getId() + ", Type: " + n.getType() + ", Status: " + n.getStatus())
        );

        service.shutdown();
    }
}
