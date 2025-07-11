import { themes as prismThemes } from 'prism-react-renderer'
import { $axios } from './src/api'
import { remarkObsidianImage } from './src/plugins/remark.js'

/** @type {import('@docusaurus/types').Config} */
export default async function createConfigAsync() {
    const config = {
        title: 'My Site',
        tagline: 'Dinosaurs are cool',
        favicon: 'img/favicon.ico',

        // Future flags, see https://docusaurus.io/docs/api/docusaurus-config#future
        future: {
            v4: true, // Improve compatibility with the upcoming Docusaurus v4
        },

        // Set the production url of your site here
        url: 'https://blog.devstat.com',
        // Set the /<baseUrl>/ pathname under which your site is served
        // For GitHub pages deployment, it is often '/<projectName>/'
        baseUrl: '/',

        // GitHub pages deployment config.
        // If you aren't using GitHub pages, you don't need these.
        organizationName: 'facebook', // Usually your GitHub org/user name.
        projectName: 'docusaurus', // Usually your repo name.

        onBrokenLinks: 'throw',
        onBrokenMarkdownLinks: 'warn',

        // Even if you don't use internationalization, you can use this field to set
        // useful metadata like html lang. For example, if your site is Chinese, you
        // may want to replace "en" with "zh-Hans".
        i18n: {
            defaultLocale: 'en',
            locales: ['en'],
        },

        presets: [
            [
                'classic',
                /** @type {import('@docusaurus/preset-classic').Options} */
                ({
                    docs: {
                        routeBasePath: '/',
                        sidebarPath: './sidebars.ts',
                        remarkPlugins: [remarkObsidianImage],
                    },
                    blog: {
                        showReadingTime: true,
                        feedOptions: {
                            type: ['rss', 'atom'],
                            xslt: true,
                        },
                        // Useful options to enforce blogging best practices
                        onInlineTags: 'warn',
                        onInlineAuthors: 'warn',
                        onUntruncatedBlogPosts: 'warn',
                    },
                    theme: {
                        customCss: './src/css/custom.css',
                    },
                }),
            ],
        ],

        themeConfig:
            /** @type {import('@docusaurus/preset-classic').ThemeConfig} */
            ({
                // Replace with your project's social card
                image: 'img/docusaurus-social-card.jpg',
                navbar: {
                    title: 'Docusaurus',
                    logo: {
                        alt: 'My Site Logo',
                        src: 'img/logo.svg',
                    },
                    items: [
                        {
                            to: '/ABCD',
                            label: 'Who am I?',
                            position: 'left',
                        },
                        // TODO: 포트폴리오 화면 생성 후 수정해야함.
                        // {
                        //     to: '/portfolio',
                        //     label: 'Portfolio',
                        //     position: 'left',
                        // },
                        // {
                        //     href: '/login',
                        //     label: 'Login',
                        //     position: 'right',
                        // },
                        {
                            href: 'https://github.com/SubiYoon',
                            label: 'GitHub',
                            position: 'right',
                        },
                    ],
                },
                // footer: {
                //     style: 'dark',
                //     links: [
                //         {
                //             title: 'Docs',
                //             items: [
                //                 {
                //                     label: 'Tutorial',
                //                     to: '/docs/intro',
                //                 },
                //             ],
                //         },
                //         {
                //             title: 'Community',
                //             items: [
                //                 {
                //                     label: 'Stack Overflow',
                //                     href: 'https://stackoverflow.com/questions/tagged/docusaurus',
                //                 },
                //                 {
                //                     label: 'Discord',
                //                     href: 'https://discordapp.com/invite/docusaurus',
                //                 },
                //                 {
                //                     label: 'X',
                //                     href: 'https://x.com/docusaurus',
                //                 },
                //             ],
                //         },
                //         {
                //             title: 'More',
                //             items: [
                //                 {
                //                     label: 'Blog',
                //                     to: '/blog',
                //                 },
                //                 {
                //                     label: 'GitHub',
                //                     href: 'https://github.com/SubiYoon',
                //                 },
                //             ],
                //         },
                //     ],
                //     copyright: `Copyright © ${new Date().getFullYear()} My Project, Inc. Built with Docusaurus.`,
                // },
                prism: {
                    theme: prismThemes.github,
                    darkTheme: prismThemes.dracula,
                    additionalLanguages: ['java', 'bash', 'python'],
                },
            }),
    }

    await $axios.get(process.env.API_SERVER + '/info/ABCD').then(res => {
        const result = res.data;

        config.title = result.alias; // Tab Title Setting
        config.themeConfig.navbar.title = result.alias; // Header MainPageText Title
        config.presets[0][1]['docs'].path = result.directoryPath; // Docs 위치 바인딩
    })

    await $axios.get(process.env.API_SERVER + '/info/ABCD/docs').then(res => {
        const docsInfo = res.data;

        docsInfo.forEach(doc => {
            config.themeConfig.navbar.items.push(
                {
                    type: 'docSidebar',
                    sidebarId: doc.sidebarId,
                    position: 'left',
                    label: doc.sidebarId,
                },
            );
        })
    })

    return config
}
