import z from "zod";

// Zod schemas must be manually kept up to date with types generated

export const setupAccountFormSchema = z.object({
  firstName: z.string().nonempty(),
  lastName: z.string().nonempty(),
  username: z.string().nonempty(),
});

export type TSetupAccountFormSchema = z.infer<typeof setupAccountFormSchema>;
