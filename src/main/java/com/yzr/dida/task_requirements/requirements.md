📋 Task API 开发任务分配

🎯 核心接口需求

1. GET /open/v1/project/{projectId}/task/{taskId} - 获取单个任务
2. POST /open/v1/task - 创建任务
3. POST /open/v1/task/{taskId} - 更新任务
4. POST /open/v1/project/{projectId}/task/{taskId}/complete - 完成任务
5. DELETE /open/v1/project/{projectId}/task/{taskId} - 删除任务

📊 数据模型要求

根据API文档，Task实体需要包含：
- 基础字段：id, projectId, title, content, desc
- 时间字段：startDate, dueDate, completedTime, timeZone
- 状态字段：status, priority, isAllDay
- 功能字段：reminders, repeatFlag, sortOrder
- 子任务：items数组(ChecklistItem类型)