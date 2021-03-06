USE [TSPlocations]
GO
/****** Object:  Schema [DataManager]    Script Date: 2018-03-12 10:01:19 ******/
CREATE SCHEMA [DataManager]
GO
/****** Object:  Table [DataManager].[Dataset]    Script Date: 2018-03-12 10:01:19 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [DataManager].[Dataset](
	[DatasetID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](50) NOT NULL,
	[LocationsCount] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[DatasetID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
UNIQUE NONCLUSTERED 
(
	[Name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [DataManager].[Locations]    Script Date: 2018-03-12 10:01:19 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [DataManager].[Locations](
	[DatasetID] [int] NULL,
	[ID] [int] NULL,
	[X] [real] NOT NULL,
	[Y] [real] NOT NULL,
	[TagName] [varchar](20) NULL
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
ALTER TABLE [DataManager].[Locations]  WITH NOCHECK ADD FOREIGN KEY([DatasetID])
REFERENCES [DataManager].[Dataset] ([DatasetID])
GO
