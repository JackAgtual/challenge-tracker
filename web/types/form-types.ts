import z from "zod";

// Zod schemas must be manually kept up to date with types generated

export const setupAccountFormSchema = z.object({
  firstName: z.string().nonempty(),
  lastName: z.string().nonempty(),
  username: z.string().nonempty(),
});

export type TSetupAccountFormSchema = z.infer<typeof setupAccountFormSchema>;

export const challengeDataFormSchema = z.object({
  name: z.string().nonempty(),
  durationDays: z.int().min(1),
});

export type TChallengeDataFormSchema = z.infer<typeof challengeDataFormSchema>;

export const createGoalFormSchema = z.object({
  name: z.string().nonempty(),
});

export type TCreateGoalFormSchema = z.infer<typeof createGoalFormSchema>;

export const inviteUserFormSchema = z.object({
  username: z.string().nonempty(),
});

export type TInviteUserFormSchema = z.infer<typeof inviteUserFormSchema>;
